/*
 * Created on 2004-7-17
 *
 */
package net.ninecube.core;

import net.ninecube.core.domain.Entity;


/**
 * @author jxb
 * @hibernate.meta attribute="auto-generated"
 * @hibernate.class
 */
public class Bar implements Entity
{
	private Long id;
	private String name;
	
	public Bar(Long id, String name)
	{
		super();
		this.id = id;
		this.name = name;
	}

	public Bar()
	{
		super();
	}
	
	/**
	 * @hibernate.id generator-class="assigned"
	 */
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	/**
	 * @hibernate.property
	 */
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
}
