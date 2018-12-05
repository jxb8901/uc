package net.ninecube.saturn;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.ninecube.db.DBUtil;
import net.ninecube.db.MapDBManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SqlResultCheck {
	private static Log log = LogFactory.getLog(SqlResultCheck.class);
	private String expectedResult;
	private String table ;
	private List<String> columns;
	private int[] primaryKeys;
	private Map<String, String[]> results = new HashMap<String, String[]>();
	
	public SqlResultCheck(String expectedResult){
		this.expectedResult = expectedResult;
		init();
	}
	
	private void init(){
		int i = 0;
		String[] strs = expectedResult.split("\n");
		for(String str : strs){
			str = str.trim();
			if("".equals(str) || "--".equals(str)) continue;
			switch(i){
				case 0:
					table = str ; i++;
					break;
				case 1:
					columns = Arrays.asList(str.split("\\s+")); i++;
					//constructs primary keys index
					constrctPrimaryKeys();
					break;
				default:
					i++;
					String[] rec = str.split("\\s+");
					String[] rc = new String[rec.length > columns.size() ? rec.length : columns.size()];
					for(int j = 0 ; j < rec.length && j < rc.length; j++)rc[j] = rec[j];
					results.put(getPrimaryKey(rc), rc);
			}
		}
		if(i<2) throw new IllegalArgumentException("result format error ! expected the first two"+
				" lines specify the table name and column names.");
		
	}
	
	private void constrctPrimaryKeys(){
		List<Integer> pkeys = new ArrayList<Integer>();
//		log.debug("***primary keys : ");
		for(int i = 0 ; i < columns.size(); i++){
			log.debug("check : " + columns.get(i));
			if(columns.get(i).startsWith("*")){
				columns.set(i, columns.get(i).substring(1));
				pkeys.add(i);
//				log.debug(" key : " + columns.get(i));
			}
		}
		if(pkeys.isEmpty()) pkeys.add(0);
		primaryKeys = new int[pkeys.size()];
		for(int i = 0 ; i < pkeys.size(); i++){
			primaryKeys[i] = pkeys.get(i);
		}
	}
	
	private String getPrimaryKey(String[] row){
		String result= "";
		for(int i = 0 ; i < primaryKeys.length ; i++){
			result += row[primaryKeys[i]] + ">";
		}
		result = result.substring(0,result.length()-1);
		return result;
	}
	
	public void check(){
		Map<String, String[]> arsts = getActualResults();
		Map<String, String[]> ersts = new HashMap<String, String[]>(this.results);
		String errStr = "";
		int errnums = 0;
		if(ersts.size() != arsts.size())
			errStr = "\r\n" + ++errnums + ". record number error ! expected  " + ersts.size() +" , actual " + arsts.size();
		List<String> losedRecs = new ArrayList<String>();
		List<String> unmatchedRecs = new ArrayList<String>();
		for(Iterator<Map.Entry<String, String[]>> it = ersts.entrySet().iterator();it.hasNext();){
			Map.Entry<String, String[]> erec = it.next();
			if(!arsts.containsKey(erec.getKey())) {
				losedRecs.add(erec.getKey());
				continue;
			}
			if(erec.getValue().length != arsts.get(erec.getKey()).length){
				unmatchedRecs.add(erec.getKey());
			}
			boolean pass = true;
			String str = erec.getKey() + "(";
			for(int i = 0 ; i < erec.getValue().length ; i++){
				if(!equal(erec.getValue()[i], arsts.get(erec.getKey())[i])){
					str +=  this.columns.get(i) + "[" +erec.getValue()[i] + ", " + arsts.get(erec.getKey())[i] +"] ;";
					pass = false;
				}
			}
			str = str.substring(0,str.length() - 1) +  ")";
			if(!pass) unmatchedRecs.add(str);
			arsts.remove(erec.getKey());
			it.remove();
		}
		if(losedRecs.size() > 0){
			errStr += "\r\n" + ++errnums + ".  expected following records : ";
			for(String id : losedRecs) errStr += id + ",";
			errStr = errStr.substring(0,errStr.length() - 1) ;
		}

		if(unmatchedRecs.size() > 0){
			errStr +=  "\r\n" + ++errnums + ". following records can't be matched (format: id(columnName[expected value, actural value])): \r\n";
			for(String id : unmatchedRecs) errStr += id + "\r\n";
			errStr = errStr.substring(0,errStr.length() - 2) ;
		}
		if(arsts.size() > 0){
			errStr +=   "\r\n" + ++errnums + ". following records aren't expected : ";
			for(String id : arsts.keySet()) errStr += id + ",";
			errStr = errStr.substring(0,errStr.length() - 1) ;
		}
		if(errnums > 0) throw new UnmatchedResultException("Can't match the result , find " + errnums + 
				" errors, the detailed errors are listed as following :" + errStr);
	}
	
	protected boolean equal(String expected, String actual){
		DecimalFormat f = new DecimalFormat("###0.##");
		try{
			Number ne = f.parse(expected);
			Number na = f.parse(actual);
			return ne.equals(na);
		}catch(Exception e){};
		return expected.equals(actual);
	}
	
	protected Map<String, String[]> getActualResults(){
		Map<String, String[]> arsts = new HashMap<String, String[]>();
		String sql = "select ";
		for(String c : this.columns) sql += c + " ,";
		sql = sql.substring(0, sql.length() - 1);
		sql +=" from " + this.table ;
		for(Object[] rec : DBUtil.executeQuery(sql, null, 0, Integer.MAX_VALUE)){
			int maxlength = columns.size() > rec.length ? columns.size() : rec.length;
			String[] r = new String[maxlength];
			for(int i = 0 ; i < rec.length ; i++) r[i] = rec[i] == null ? "" : rec[i].toString();
			arsts.put(getPrimaryKey(r), r);
		}
		return arsts;
	}

	
	public static class UnmatchedResultException extends RuntimeException{
		public UnmatchedResultException(String errMsg){
			super(errMsg);
		}
	}
	
}
