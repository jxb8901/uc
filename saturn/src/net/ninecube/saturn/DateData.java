package net.ninecube.saturn;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.ninecube.util.DateUtil;
import net.ninecube.util.StringUtil;

public class DateData {
	public static enum Type{
		Y("(\\d{4})年"),
		Q("(\\d{1,2})季度"),
		M("(\\d{1,2})月"),
		D("(\\d{1,2})日"),
		YQ("(\\d{4})年(\\d{1,2})季度"),
		YM("(\\d{4})年(\\d{1,2})月"),
		MD("(\\d{1,2})月(\\d{1,2})日"),
		YMD("(\\d{4})年(\\d{1,2})月(\\d{1,2})日");
		
		private Pattern pattern;
		
		Type(String pattern){
			this.pattern = Pattern.compile(pattern);
		}
		
	}
	
	private Type type;
	private int year,quarter,month,day;
	private String literal;
	
	public Type getType(){
		return type;
	}
	
	public String getSql(){
		String code = "";
		if(year != 0) code += year;
		if(quarter != 0) code += StringUtil.lpadding(""+quarter,2,"0");
		if(month != 0) code += StringUtil.lpadding(""+month,2,"0");
		if(day != 0) code += StringUtil.lpadding(""+day,2,"0");
		return code;
	}
	
	public String toString(){
		return literal;
	}
	
	private boolean valid(){
		if(quarter != 0) if(quarter<1 || quarter >4) return false;
		if(month != 0) if(month <1 || month >12) return false;
		if(day != 0){
			if(day <1 || day >31) return false;
			if(type != Type.D){
				String s = getSql();
				if(year == 0) s = "2000" + s;
				if(DateUtil.parseDate(s,"yyyyMMdd") == null) return false;				
			}
		}
		return true;
	}
	
	public static DateData parse(String literal){
		for(Type t : Type.values()){
			Matcher m = t.pattern.matcher(literal);
			if(m.matches()){
				DateData ret = new DateData();
				ret.type = t; ret.literal = literal;
				switch(t){
				case Y : ret.year = Integer.parseInt(m.group(1)) ; break;
				case Q : ret.quarter = Integer.parseInt(m.group(1)) ; break;
				case M : ret.month = Integer.parseInt(m.group(1)) ; break;
				case D : ret.day = Integer.parseInt(m.group(1)) ; break;
				case YQ : ret.year = Integer.parseInt(m.group(1)) ; ret.quarter = Integer.parseInt(m.group(2)) ; break;
				case YM : ret.year = Integer.parseInt(m.group(1)) ; ret.month = Integer.parseInt(m.group(2)) ; break;
				case MD : ret.month = Integer.parseInt(m.group(1)) ; ret.day = Integer.parseInt(m.group(2)) ; break;
				case YMD : ret.year = Integer.parseInt(m.group(1)) ; ret.month = Integer.parseInt(m.group(2)) ;  ret.day = Integer.parseInt(m.group(3)) ; break;
				}
				if(!ret.valid()) break;
				return ret;
			}
		}
		throw new IllegalArgumentException("illegal date type : " + literal);
	}
}
