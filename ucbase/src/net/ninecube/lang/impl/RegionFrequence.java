package net.ninecube.lang.impl;

import java.util.Calendar;
import java.util.Date;

import net.ninecube.lang.Frequence;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.util.DateUtil;

public class RegionFrequence extends Frequence {

	public RegionFrequence() {
		super("R");
	}
	
	public boolean endDay(Date date){
		return false;
	}

	@Override
	public String add(Date date, int i) {
		throw new UnsupportedOperationException("Unsupported Operation Exception !");
	}
	
	@Override
	public String[] getNames() {
		return new String[] { "R", "区间" ,"结束" };
	}

	@Override
	protected void parse(String str, FrequenceDate f) {
		String[] strs = str.split("\\-"); 
		if (str.length() != 17 && strs.length != 2) {
			throw new IllegalArgumentException("fromat error:" + type + str);
		}
		f.set(DateUtil.parseDate(strs[0], "yyyyMMdd"), DateUtil.parseDate(strs[1], "yyyyMMdd"));
		return;
	}

	protected void parse(Date date, FrequenceDate f){
		throw new UnsupportedOperationException("Unsupported Operation Exception !");
	}

	@Override
	protected int sub(FrequenceDate d1, FrequenceDate d2) {
		throw new UnsupportedOperationException("Unsupported Operation Exception !");
}

	@Override
	protected int toSeq() {
		return 40;
	}

	@Override
	public String getFrequenceDateStr(FrequenceDate f) {
		String rst = getType();
		rst += f.getStartDateString();
		rst += "-";
		rst += f.getEndDateString();
		return rst;
	}

}
