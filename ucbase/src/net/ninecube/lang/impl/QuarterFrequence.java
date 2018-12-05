package net.ninecube.lang.impl;

import java.util.Calendar;
import java.util.Date;

import net.ninecube.lang.Frequence;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.util.DateUtil;

public class QuarterFrequence extends Frequence {

	public QuarterFrequence() {
		super("Q");
	}
	
	public boolean endDay(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return (c.get(Calendar.MONTH) + 1) % 3 == 0 && c.getMaximum(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH);
	}

	@Override
	public String add(Date date, int i) {
		String ret = this.type;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, i * 3);
		ret += c.get(Calendar.YEAR) + "0" + (c.get(Calendar.MONTH) / 3 + 1);
		return ret;
	}

	@Override
	public String[] getNames() {
		return new String[] { "Q", "季" };
	}

	@Override
	protected void parse(String str, FrequenceDate f) {
		if (str.length() != 6) {
			throw new IllegalArgumentException("fromat error:" + type + str);
		}
		int year = Integer.parseInt(str.substring(0, 4));
		int seq = Integer.parseInt(str.substring(4));
		String fstr = "" + year + ((seq -1) * 3 + 1);
		Date date = DateUtil.parseDate(fstr, "yyyyMM");
		parse(date, f);
		return;
	}

	protected void parse(Date date, FrequenceDate f){
		Date d = DateUtil.digestParts(date, "yyyyMM");
		Calendar s = Calendar.getInstance();
		s.setTime(d);
		s.set(Calendar.MONTH, s.get(Calendar.MONTH) / 3 * 3);
		d = s.getTime();
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.MONTH, 3);
		f.set(d, c.getTime());
	}

	@Override
	protected int sub(FrequenceDate d1, FrequenceDate d2) {
		Calendar c = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c.setTime(d1.getStartDate());
		c2.setTime(d2.getStartDate());
		return (c.get(Calendar.YEAR) - c2.get(Calendar.YEAR)) * 4 + 
		getQuarterSeq(d1.getStartDate()) - getQuarterSeq(d2.getStartDate());
	}
	
	private int getQuarterSeq(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int seq = c.get(Calendar.MONTH) / 3 + 1;
		return seq;
	}

	@Override
	protected int toSeq() {
		return 30;
	}

	@Override
	public String getFrequenceDateStr(FrequenceDate f) {
		String rst = getType();
		rst += DateUtil.getYYYY(f.getStartDate())  + "0" + getQuarterSeq(f.getStartDate());
		return rst;
	}

}
