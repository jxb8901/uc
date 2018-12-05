/**
 * created on 2006-4-10
 */
package net.ninecube.lang;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.ninecube.lang.impl.RegionFrequence;
import net.ninecube.util.DateUtil;
import net.ninecube.util.StringUtil;

/**
 * 频度时间，使用三元组表示（频度，起始日期，结束日期）
 * ('Q', 20060101, 20060401) 表示2006年第1季度
 * ('D', 20060103, 20060104) 表示2006年1月3日
 * ('D', 20060203, 20060204) 表示2006年2月3日
 * ('M', 20061201, 20070101) 表示2006年12月
 * 
 * 频度时间的字符串表示格式如下：
 * DyyyyMMdd
 * MyyyyMM
 * QyyyyQQ
 * Yyyyy
 * RyyyyMMdd-yyyyMMdd 区间时间
 * 
 * TODO:当前月频度，季频度时间会自动以自然月结束为标志。而不是以开始时间与结否时间之间的区间来计算。
 * 
 * @author JXB
 * 
 */
public class FrequenceDate implements Comparable {
	
	private Frequence freq;
	private Date start, end;
	private String freqDateStr;
	
	public FrequenceDate(String time) {
		if (time == null || time.length() < 5) 
			throw new IllegalArgumentException("format error: '" + time + "'");
		this.freqDateStr = time;
		this.freq = Frequence.get(time.substring(0, 1));
		this.freq.parse(time.substring(1), this);
	}
	
	
	
	public FrequenceDate(Date date, Frequence freq){
		this.freq = freq;
		freq.parse(date, this);
		this.freqDateStr = this.freq.getFrequenceDateStr(this);
	}
	
	/**
	 * @param startDate
	 * @param endDate
	 * @param freq
	 * TODO:由FrequenceDate所表示的时间区间包括开始时间，但不包括结束时间。
	 */
	public FrequenceDate(Date startDate, Date endDate, Frequence freq){
		this.start = startDate;
		this.end = endDate;
		this.freq = freq;
		try {
			freq.parse(startDate, this);
		} catch (UnsupportedOperationException e) {}
		this.freqDateStr = this.freq.getFrequenceDateStr(this);
	}
	

	public Frequence getFrequence() {
		return freq;
	}
	
	/**
	 * 返回频度时间的标准日期表示，非“日”的频度日期全部表示为该期间的期初日期
	 * 如Q200601对应的日期为20060101，M200605对应的日期为20060501
	 * Y2005对应的日期为20050101
	 */
	public Date toDate() {
		return getStartDate();
	}
	
	public String toDateString() {
		return getEndDateString();
	}
	
	/**
	 * 取期末日期
	 * Y2006: 20060101 - 20070101
	 * Q200601: 20060101 - 20060401
	 * M200603: 20060301 - 20060401
	 * D20060322: 20060322 - 20060323
	 */
	public Date getEndDate() {
		return end;
	}
	
	/**
	 *如果isInclusive == false , 等同于getEndDate();
	 *如果isInclusive == true  
	 * Y2006: 20060101 - 20061231
	 * Q200601: 20060101 - 20060331
	 * M200603: 20060301 - 20060331
	 * D20060322: 20060322 - 20060322
	 *
	 */
	public  Date getEndDate(boolean isInclusive){
		if(!isInclusive) return getEndDate();
		Calendar c = Calendar.getInstance();
		c.setTime(getEndDate());
		c.add(Calendar.DAY_OF_MONTH, -1);
		return new java.sql.Date(c.getTime().getTime());
	}
	
	public String getEndDateString() {
		return DateUtil.getYYYYMMDD(this.end);
	}
	
	public String getEndDateString(boolean isInclusive) {
		return DateUtil.getYYYYMMDD(getEndDate(isInclusive));
	} 
	
	/**
	 * 取期初日期
	 */
	public Date getStartDate() {
		return start;
	}
	
	public String getStartDateString() {
		return DateUtil.getYYYYMMDD(this.start);
	}

	public FrequenceDate cast(Frequence targetFrequence) {
		return new FrequenceDate(start, targetFrequence);
	}
	
	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + freq.hashCode();
		hash = hash *31 + this.start.hashCode();
		hash = hash *31 + this.end.hashCode();
		return hash; 
	}
	
	public boolean equals(Object o) {
		return this.compareTo(o) == 0;
	}
	
	public String toString() {
		return freqDateStr;
	}
	
	public FrequenceDate add(int step) {
		return new FrequenceDate(this.freq.add(this.start, step));
	}
	
	public int sub(FrequenceDate other) {
		if (!other.freq.equals(this.freq)) throw new IllegalArgumentException("frequence not same!");
		return this.freq.sub(this, other);
	}

	public int compareTo(Object o) {
		if (o instanceof FrequenceDate) {
			FrequenceDate f1 = (FrequenceDate) o;
			int i = this.freq.compareTo(f1.freq);
			if ( i != 0 ) return i;
			i = this.start.compareTo(f1.start);
			if (i != 0 ) return i;
			i = this.end.compareTo(f1.end);
			return i;
		}
		return -1;
	}
	
	public void set(java.util.Date start, java.util.Date end) {
		this.start = new java.sql.Date(start.getTime());
		this.end = new java.sql.Date(end.getTime());
	}

	public void setFreqDateStr(String freqDateStr) {
		this.freqDateStr = freqDateStr;
	}
	
	public static List<FrequenceDate> getAllEndFrequenceDate(Date date){
		List<FrequenceDate> freqDates = new ArrayList<FrequenceDate>();
		for(Frequence f : Frequence.getAllFrequence()){
			if(f.endDay(date))
				freqDates.add(new FrequenceDate(date, f));
		}
		return freqDates;
	}
}
