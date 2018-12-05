/**
 * 
 * created on 2007-6-14
 */
package net.ninecube.core.task.impl;

import java.util.Date;

/**
 * 
 * @author jxb
 */
public class TestTask implements Runnable {
	private int count = 0;
	private int id;
	private Date date;
	private String string;
	private boolean booleanValue; 
	private TestEnumType type;

	public void run() {
		count++;
		System.err.println("***********  " + count);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public TestEnumType getType() {
		return type;
	}

	public void setType(TestEnumType type) {
		this.type = type;
	}
}
