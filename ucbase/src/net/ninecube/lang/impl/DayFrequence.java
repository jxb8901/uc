package net.ninecube.lang.impl;

import java.util.Calendar;
import java.util.Date;

import net.ninecube.lang.Frequence;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.util.DateUtil;

public class DayFrequence extends Frequence {

	public DayFrequence() {
		super("D");
	}
	
	public boolean endDay(Date date){
		return true;
	}

	@Override
	public String add(Date date, int i) {
		String ret = this.type;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, i);
		ret += DateUtil.getYYYYMMDD(c.getTime());
		return ret;
	}

	public String[] getNames() {
		return new String[] { "D", "天" };
	}

	protected void parse(String str, FrequenceDate f) {
		Date date = DateUtil.parseDate(str, "yyyyMMdd");
		if (date == null) {
			throw new IllegalArgumentException("fromat error:" + type + str);
		}
		parse(date, f);
		return;
	}

	protected void parse(Date date, FrequenceDate f){
		Date d = DateUtil.digestParts(date, "yyyyMMdd");
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.DAY_OF_MONTH, 1);
		f.set(d,c.getTime());
		
	}
	
	@Override
	protected int sub(FrequenceDate d1, FrequenceDate d2) {
		return (int) ((d1.getEndDate().getTime() - d2.getEndDate().getTime())
				/ (1000 * 60 * 60 * 24) + 0);
	}

	@Override
	protected int toSeq() {
		return 10;
	}

	@Override
	public String getFrequenceDateStr(FrequenceDate f) {
		String rst = getType();
		rst += DateUtil.getYYYYMMDD(f.getStartDate());
		return rst;
	}

}
