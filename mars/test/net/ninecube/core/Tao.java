/*
 * Created on 2004-7-17
 *
 */
package net.ninecube.core;


/**
 * @author jxb
 */
public class Tao
{
	private char initial;
	private String first;
	private String last;
	
	public Tao() {}
	
	public Tao(char initial, String first, String last)
	{
		super();
		this.initial = initial;
		this.first = first;
		this.last = last;
	}

	/**
	 * @hibernate.property column="F"
	 */
	public String getFirst()
	{
		return first;
	}
	public void setFirst(String first)
	{
		this.first = first;
	}
	/**
	 * @hibernate.property column="I"
	 */
	public char getInitial()
	{
		return initial;
	}
	public void setInitial(char initial)
	{
		this.initial = initial;
	}
	/**
	 * @hibernate.property column="L"
	 */
	public String getLast()
	{
		return last;
	}
	public void setLast(String last)
	{
		this.last = last;
	}
}
