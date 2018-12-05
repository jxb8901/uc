/**
 * created on 2006-4-12
 */
package net.ninecube.lang;

import java.util.Date;
import java.util.Iterator;

/**
 * @author JXB
 */
public class DateRange implements Iterable<FrequenceDate> {

	private FrequenceDate start;
	private FrequenceDate end;
	
	public DateRange(FrequenceDate start, FrequenceDate end) {
		this.start = start;
		this.end = end;
		if (!start.getFrequence().equals(end.getFrequence()))
			throw new IllegalArgumentException("频度必须一致");
		this.checkOrder();
	}
	
	public DateRange(Frequence freq, Date start, Date end) {
		this.start = new FrequenceDate(start, freq);
		this.end = new FrequenceDate(end, freq);
		this.checkOrder();
	}
	
	/**
	 * start = end - count
	 * @param freq 
	 * @param end
	 * @param count
	 */
	public DateRange(Frequence freq, Date end, int count) {
		this(new FrequenceDate(end, freq), count);
	}
	public DateRange(FrequenceDate end, int count) {
		this.end = end;
		this.start = this.end.add(0 - count);
		this.checkOrder();
	}
	
	private void checkOrder() {
		// 保证start总是小于end
		if (this.start.compareTo(this.end) > 0) {
			FrequenceDate tmp = this.start;
			this.start = this.end;
			this.end = tmp;
		}
	}
	
	public Frequence getFrequence() {
		return this.start.getFrequence();
	}

	public Iterator<FrequenceDate> iterator() {
		return new Iterator<FrequenceDate>() {
			FrequenceDate current = start;
			public void remove() {
				throw new UnsupportedOperationException();
			}

			public boolean hasNext() {
				return current.compareTo(end) <= 0;
			}

			public FrequenceDate next() {
				FrequenceDate ret = current;
				current = current.add(1);
				return ret;
			}
		};
	}

	public int size() {
		return size(start.getFrequence());
	}
	
	public int size(Frequence fre) {
		return end.sub(start) + 1;
	}
	
	/**
	 * 得到指定日期在本DateRange中的序号(第几天),序号从1开始.
	 * @param date
	 * @return
	 */
	public int getIndex(Date date){
		int index = 1;
		FrequenceDate freqDate = new FrequenceDate(date, start.getFrequence());
		if(freqDate.compareTo(start) < 0 || freqDate.compareTo(end) > 0){
			throw new IllegalArgumentException("invalid argument : date : " + date + " , in DataRange : " + this  );
		}
		return (int)freqDate.sub(start);
	}

	public FrequenceDate getEnd() {
		return end;
	}

	public FrequenceDate getStart() {
		return start;
	}
	
	public String toString() {
		return this.start + "-" + this.end;
	}
}
