package net.ninecube.lang.impl;

import java.util.Calendar;
import java.util.Date;

import net.ninecube.lang.Frequence;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.util.DateUtil;

public class YearFrequence extends Frequence {

	public YearFrequence() {
		super("Y");
	}
	
	public boolean endDay(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH) == 11 && c.getMaximum(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH);
	}

	@Override
	public String add(Date date, int i) {
		String ret = this.type;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, i);
		ret += c.get(Calendar.YEAR);
		return ret;
	}

	@Override
	public String[] getNames() {
		return new String[] { "Y", "年" };
	}

	@Override
	protected void parse(String str, FrequenceDate f) {
		if (str.length() != 4) {
			throw new IllegalArgumentException("fromat error:" + type + str);
		}
		Date date = DateUtil.parseDate(str, "yyyy");
		parse(date, f);
		return;
	}

	protected void parse(Date date, FrequenceDate f){
		Date d = DateUtil.digestParts(date, "yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.YEAR, 1);
		f.set(d, c.getTime());
	}

	@Override
	protected int sub(FrequenceDate d1, FrequenceDate d2) {
		Calendar c = Calendar.getInstance();
		c.setTime(d1.getStartDate());
		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2.getStartDate());
		return c.get(Calendar.YEAR) - c2.get(Calendar.YEAR);	
	}

	@Override
	protected int toSeq() {
		return 40;
	}

	@Override
	public String getFrequenceDateStr(FrequenceDate f) {
		String rst = getType();
		rst += DateUtil.getYYYY(f.getStartDate());
		return rst;
	}

}
