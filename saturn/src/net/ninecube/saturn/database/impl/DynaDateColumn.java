package net.ninecube.saturn.database.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.ninecube.saturn.DateData;
import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.DatabaseException;
import net.ninecube.saturn.database.Table;
import net.ninecube.util.DateUtil;
import net.ninecube.util.StringUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jrc.contribute.Jrc;
import org.codehaus.jrc.expression.Expression;
import org.codehaus.jrc.expression.Expressions;


/**
 * 动态日期字段
 * 可针对日期字段实现“年月”，“月日”等子字段
 * 目前全部转换为字符串再调用substring函数取相应日期部分值
 * 
 * @author jxb
 *
 */
public class DynaDateColumn implements Column {
	private static Log log = LogFactory.getLog(DynaDateColumn.class);
	
	protected static enum Type {
		Y("yyyy","年,年份,Y"), 
		//Q("Q","季度"),
		M("MM","月,月份,M"), 
		D("dd","日,日期,D"),
		//YQ("YQ","年季"),
		YM("yyyyMM","年月,YM"), 
		MD("MMdd","月日,MD"),
		YMD("yyyyMMdd","年月日,YMD");
		

		static final String FULL_PATTERN = "yyyyMMdd";
		private String pattern;
		private List<String> alias;
		private int groupNum = 0;
		List<String> groups = new ArrayList<String>();
		private int startIndex;
		
		Type(String pattern, String alias) {
			this.pattern = pattern;
			this.alias = StringUtil.split(alias);
			init();
		}
		
		private void init(){
			Pattern p = Pattern.compile("(yyyy)?(MM)?(dd)?");
			Matcher m = p.matcher(pattern);
			if(!m.find()) throw new IllegalArgumentException("unexpected pattern '" + pattern + "'");
			for(int i = 1 ; i <= m.groupCount() ; i++){
				groups.add(m.group(i) == null ? "" : m.group(i));
				if(m.group(i) != null && m.group(i).length() > 0) groupNum++;
//				log.debug("" + i + "." + m.group(i));
			}
			startIndex = FULL_PATTERN.indexOf(pattern);
			if(groupNum == 0 || startIndex == -1)  throw new IllegalArgumentException("unexpected pattern '" + pattern + "'");
		}
		
		public static Type get(String s) {
			for (Type t : Type.values()) {
				if (t.alias.contains(s)) return t;
			}
			return null;
		}
		
		public int getStartIndex(boolean hasHyphen){
			if(!hasHyphen)
				return startIndex + 1;
			int index = startIndex;
			for(int i = 0 ; i < groups.size(); i++){
				if(i > 0) index++;
				if(groups.get(i).length() > 0) break;
			}
			return index + 1;
		}
		
		public int getLength(boolean hasHyphen){
			if(!hasHyphen) return pattern.length();
			return pattern.length() + groupNum - 1;
		}
		
		public String getPattern(boolean hasHyphen){
			if(!hasHyphen) return pattern;
			String p = "";
			for(int i = 0 ; i < groups.size() ; i++) p += "-" + groups.get(i);
			p = p.replaceAll("^(-*)([yMd]+(?:-*[Md]+)*)(-*)$","$2");
			return p;
		}
		
		public DateData.Type getType(){
			return DateData.Type.valueOf(name());
		}
	}
	
	private ColumnImpl owner;
	private Type type;
	private String name;
	private List<String> alias;
	private boolean hasHyphen = false;
	
	public DynaDateColumn(ColumnImpl owner, String type) {
		if (!ColumnImpl.Type.dyna.equals(owner.getType()))
			throw new IllegalArgumentException(
					"column's type must be 'dyna', column name:"+owner.getName());
		this.owner = owner;
		this.type = Type.get(type);
		if (this.type == null) throw new IllegalArgumentException("un recogernize: " + type);
		this.name = owner.getName() + "." + this.type;
		this.alias = new ArrayList<String>(owner.getAlias().size());
		for (String a : owner.getAlias()) this.alias.add(a + "." + this.type);
		if(owner.getPattern() != null && owner.getPattern().indexOf("-") != -1)
			hasHyphen = true;
	}
	
	//~ delegate method implements Column

	public String getName() {
		return name;
	}

	public List<String> getAlias() {
		return alias;
	}

	public boolean hasAlias(String name) {
		return this.name.equals(name) || this.alias.contains(name);
	}

	public Table getOwner() {
		return owner.getOwner();
	}
	
	private String getDatabaseType() {
		return this.owner.getOwner().getOwner().getType();
	}
	
	private String getVarcharSqlCode() {
		if (ColumnImpl.SqlType.varchar == this.owner.getSqlType())
			return this.owner.getFullName();
		// TODO: 函数的数据库相关性
		return "date_format(" + this.owner.getFullName() + ", '%Y%m%d')";
	}

	public Expression getSqlCode() {
		return Jrc.getParser().parseExpression("substring(" + this.getVarcharSqlCode() + ", " + this.type.getStartIndex(hasHyphen) + ", " + this.type.getLength(hasHyphen) + ")");
	}

	public Expression getSqlValue(Object val) {
		if(val instanceof DateData){
			if(!((DateData)val).getType().equals(type.getType())) throw new DatabaseException("日期类型不符，期待 : " + type.name());
			return Expressions.literal(((DateData)val).getSql());
		}
		return owner.getSqlValue(val);
	}

	public String getFullName() {
		return owner.getName() + "." + getName();
	}

	public boolean equals(Object o) {
		if (!(o instanceof DynaDateColumn)) return false;
		DynaDateColumn c = (DynaDateColumn) o;
		return c.getOwner().equals(this.owner) && c.getName().equals(this.name);
	}
	
	public int hashCode() {
		return this.owner.hashCode() * 31 + this.name.hashCode();
	}
}
