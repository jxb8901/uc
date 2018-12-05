package net.ninecube.lang.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.ninecube.lang.Frequence;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.util.DateUtil;

public class MonthFrequence extends Frequence {

	public MonthFrequence() {
		super("M");
	}
	
	public boolean endDay(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getMaximum(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH);
	}

	@Override
	public String add(Date date, int i) {
		String ret = this.type;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, i);
		ret += DateUtil.getYYYYMMDD(c.getTime()).substring(0, 6);
		return ret;
	}

	@Override
	public String[] getNames() {
		return new String[] { "M", "月" };
	}

	@Override
	protected void parse(String str, FrequenceDate f) {
		if (str.length() != 6) {
			throw new IllegalArgumentException("fromat error:" + type + str);
		}
		Date date = DateUtil.parseDate(str, "yyyyMM");
		parse(date, f);
	}

	protected void parse(Date date, FrequenceDate f){
		Date d = DateUtil.digestParts(date, "yyyyMM");
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.MONTH, 1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		f.set(d, c.getTime());
	}


	@Override
	protected int sub(FrequenceDate d1, FrequenceDate d2) {
		Calendar c = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c.setTime(d1.getStartDate());
		c2.setTime(d2.getStartDate());
		return (c.get(Calendar.YEAR) - c2.get(Calendar.YEAR)) * 12 + c.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
	}

	@Override
	protected int toSeq() {
		return 20;
	}

	@Override
	public String getFrequenceDateStr(FrequenceDate f) {
		String rst = getType();
		DateFormat fmt = new SimpleDateFormat("yyyyMM");
		rst += fmt.format(f.getStartDate());
		return rst;
	}

}
