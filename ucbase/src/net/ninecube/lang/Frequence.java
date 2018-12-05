/**
 * created on 2006-4-12
 */
package net.ninecube.lang;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ninecube.lang.impl.DayFrequence;
import net.ninecube.lang.impl.MonthFrequence;
import net.ninecube.lang.impl.QuarterFrequence;
import net.ninecube.lang.impl.RegionFrequence;
import net.ninecube.lang.impl.YearFrequence;
import net.ninecube.util.DateUtil;

/**
 * 频度，频度有粒度之分，“年”这个频度的粒度比“季”这个频度的粒度要大（粗）， 周的粒度比月的粒度要小（细）。
 * 
 * @author JXB
 */
public abstract class Frequence implements Comparable {

	public static final String KEY = "频度";

	public static final Frequence DAY = new DayFrequence();
	 public static final Frequence MONTH = new MonthFrequence();
	 public static final Frequence QUARTER = new QuarterFrequence();
	 public static final Frequence YEAR = new YearFrequence();
	 public static final Frequence REGION = new RegionFrequence();

	protected String type;
	protected static  Map<String,Frequence> freqMap = new HashMap<String, Frequence>();
	protected static Map<String,Frequence> fullFreqMap = new HashMap<String, Frequence>();
	protected static List<Frequence> ALL = new ArrayList<Frequence>();
	

	public abstract String add(Date date, int i);
	
	protected abstract int sub(FrequenceDate d1, FrequenceDate d2);
	
	protected abstract void parse(String str, FrequenceDate f);

	protected abstract void parse(Date date, FrequenceDate f);
	
	protected abstract int toSeq();

	public abstract String[] getNames();
	
	public abstract String getFrequenceDateStr(FrequenceDate f);
	
	public abstract boolean endDay(Date date);
	
	static {
		regist(DAY);
		regist(MONTH);
		regist(QUARTER);
		regist(YEAR);
		regist(REGION);
	}
	
	static private void regist(Frequence fre){
		freqMap.put(fre.getType(), fre);
		for(String name : fre.getNames()){
			fullFreqMap.put(name, fre);
		}
		ALL.add(fre);
	}
	
	public static List<Frequence> getAll() {
		return ALL;
	}
	
	protected Frequence(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public int hashCode() {
		return this.toSeq();
	}

	public boolean equals(Object o) {
		if (o instanceof Frequence) {
			return this.toSeq() == ((Frequence) o).toSeq();
		}
		return false;
	}

	public int compareTo(Object o) {
		if (o instanceof Frequence) {
			return this.toSeq() - ((Frequence) o).toSeq();
		}
		return -1;
	}

	public String toString() {
		return type;
	}

	public static Frequence get(String type) {
		Frequence rst = fullFreqMap.get(type);
		if(rst != null) return rst;
		throw new IllegalArgumentException("unsupported frequence: '" + type
				+ "'");
	}
	
	public static List<Frequence> getAllFrequence(){
		return new ArrayList<Frequence>(freqMap.values());
	}

}
