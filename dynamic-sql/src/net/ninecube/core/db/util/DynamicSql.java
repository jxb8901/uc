/**
 * 2007-9-6
 */
package net.ninecube.core.db.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 处理动态SQL语句的类，具有如下特性：
 * 1、支持SQL语句中的命名参数：查询参数通过命名参数实现，参数名称以:开头；
 * 2、支持SQL语句的动态拼装：若某条件的相应参数未出现，自动将相关条件从SQL语句中去除；
 * 3、支持日期数据类型：这样形式的:{name}参数是日期参数
 * 
 * @author swd, jxb
 *
 */
public class DynamicSql{
	private static final String OPERATOR_PATTERN = "(like)|(<>)|(>=)|(<=)|(<)|(>)|(=)";
	private static final String TOKEN_PATTERN = OPERATOR_PATTERN + "|(\\()|(\\))";
	
	public static String parseSql(String sql, Map<String, Object> paras){
		return parseSql(sql, paras, null);
	}
	
	public static DynamicSql getDyanamicSql(String sql){
		return new DynamicSql(sql);
	}
	
	public static String parseSql(String sql, Map<String, Object> paras, String datePattern){
		DynamicSql dsql = new DynamicSql(sql);
		if(datePattern != null) dsql.setDatePattern(datePattern);
		return dsql.getSql(paras);
	}
	
	public static String getCountSql(String sql){
		return "select count(*) from (" + sql + ") as a";
	}
	
	private String sql;
	private List<String> tokens; 
	private List<String> parameterNames;
	private String datePattern = "yyyyMMdd";
	
	public DynamicSql(String sql){
		this.sql = sql;
		init();
	}
	
	/**
	 * 取动态SQL中的参数名称
	 */
	public List<String> getParameterNames(){
		return this.parameterNames;
	}
	
	/**
	 * 将参数带入动态SQL中，生成数据库可执行的SQL语句
	 */
	public String getSql(Map<String, Object> paras){
		if (paras == null) paras = Collections.emptyMap();
		List<String> retTokens = new ArrayList<String>();
		for(int i = 0 ; i < tokens.size() ; i++){
			String t = (String)tokens.get(i);
			if(!t.startsWith(":")){retTokens.add(t); continue;}
			String var = t.substring(1);
			boolean isDate = false;
			if(var.startsWith("{") && var.endsWith("}")){
				var =var.substring(1,var.length() - 1);
				isDate = true;
			}
			Object oval = getValidValue(paras.get(var));
			if(oval != null){
				// 操作符为 in 
				if(i >=3 && "(".equals(tokens.get(i-1)) && "in".equalsIgnoreCase(tokens.get(i-2))){
					Object[] val = (oval instanceof Object[]) ? (Object[])oval : new Object[]{oval};
					for(int j = 0; j < val.length; j++){
						retTokens.add(translate(val[j], isDate, false));
						retTokens.add(",");
					}
					retTokens.remove(retTokens.size() - 1);
				} else if (i >= 1 && "like".equalsIgnoreCase(tokens.get(i - 1))) {
					retTokens.add(translate(oval, false, true));
				} else {
					retTokens.add(translate(oval, isDate, false));
				}
				continue;
			}

			if(i >=3 && "(".equals(tokens.get(i-1)) && "in".equalsIgnoreCase(tokens.get(i-2))){
				retTokens.subList(retTokens.size()-3>0?retTokens.size()-3:0, retTokens.size()).clear();
				retTokens.add("1=1");
				i++;
			}else if(i >= 2 && isOperator((String)tokens.get(i-1))){
				retTokens.subList(retTokens.size()-2>0?retTokens.size()-2:0, retTokens.size()).clear();
				retTokens.add("1=1");
				continue;
			}else if(i < tokens.size() -2 && isOperator((String)tokens.get(i+1))){
				i +=2;
				retTokens.add("1=1");
				continue;
			}else{
				throw new IllegalArgumentException(" expected a token before or after of token '" + t 
						+"', in tokens " + Util.join(" ", tokens.subList(0, i)));
			}
		}
		
		return Util.join(" ", retTokens);
	}
	
	private Object getValidValue(Object val){
		if(val == null) return null;
		if(val instanceof Object[]){
			Object[] aval = (Object[]) val;
			List<Object> l = new ArrayList<Object>();
			for(int i = 0 ; i < aval.length ; i++){
				if(!Util.isEmpty((String)aval[i])) l.add(aval[i]);
			}
			if(l.size() == 0) return null;
			if(l.size() == 1) return l.get(0);
			return l.toArray();
		}
		String sval = (String) val;
		if(Util.isEmpty(sval)) return null;
		return val;
	}
	
	private String translate(Object val, boolean isDate, boolean isLike){
		String ret = (String) ((val instanceof Object[]) ? ((Object[])val)[0] : val);
		if(isDate) {
			Date d = Util.parseDate(ret,datePattern);
			if (d == null) {
				throw new RuntimeException("parse date error: '" + ret + "' with pattern: '" + datePattern + "'");
			}
			else ret = Util.format(d, "yyyy-MM-dd");
		}
		if (isLike) return "'%" + ret.replace("'", "''") + "%'";
		return "'" + ret.replace("'", "''") + "'";
	}
	
	private void init(){
		String s = sql.replaceAll(TOKEN_PATTERN, " $0 ");
		s = s.replaceAll("(\\r)|(\\n)", " ");
		tokens = Arrays.asList(s.split("\\s+"));
		parseParameterNames();
	}
	
	private void parseParameterNames() {
		parameterNames = new ArrayList<String>();
		for(int i = 0 ; i < tokens.size(); i++){
			String t = getVariableName(tokens.get(i));
			if(t != null && ! parameterNames.contains(t))
				parameterNames.add(t);
		}
	}
	
	private String getVariableName(String token){
		if(!token.startsWith(":")) return null;
		token = token.substring(1);
		if(token.startsWith("{") && token.endsWith("}")) token = token.substring(1, token.length() -1);
		return token;
	}
	
	public boolean isOperator(String token){
		String pattern = "^("+OPERATOR_PATTERN+")$";
		return token.matches(pattern);
	}
	
	public void setDatePattern(String pattern){
		datePattern = pattern;
	}
	
	public static class Util {
		public static String join(String limit, List<String> args) {
			if (args == null || args.size() == 0) return "";
			if (args.size() == 1) return args.get(0);
			
			StringBuffer ret = new StringBuffer();
			ret.append(args.get(0));
			for (int i = 1; i < args.size(); i++)
				ret.append(limit).append(args.get(i));
			return ret.toString();
		}
		
		public static boolean isEmpty(Object o) {
			return o == null || o.toString().length() == 0;
		}
	    
	    public static String format(Date date, String pattern){
	    	SimpleDateFormat f = new SimpleDateFormat(pattern);
	    	return f.format(date);
	    }
	    
	    public static Date parseDate(String date, String pattern) {
	    	SimpleDateFormat f = new SimpleDateFormat(pattern);
	    	f.setLenient(false);
	    	try {
				return f.parse(date);
			} catch (ParseException e) {
				return null;
			}
	    }
	}
}

