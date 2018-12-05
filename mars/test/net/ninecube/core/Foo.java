/*
 * Created on 2004-7-17
 *
 */
package net.ninecube.core;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.ninecube.core.domain.Entity;


/**
 * @author jxb
 * @hibernate.meta attribute="auto-generated"
 * @hibernate.class
 */
public class Foo implements Entity
{
	private Long id;
	private String name;
	private Date date;
	private BigDecimal amount;
	private Bar bar;
	private Tao tao;
	
	private Map<String, String> properties = new HashMap<String, String>();
	
	private Set bars;
	
	public Foo(Long id, String name, Date date, BigDecimal amount)
	{
		super();
		this.id = id;
		this.name = name;
		this.date = date;
		this.amount = amount;
	}

	public Foo()
	{
		super();
	}
	
	/**
	 * @hibernate.property
	 */
	public BigDecimal getAmount()
	{
		return amount;
	}
	public void setAmount(BigDecimal amount)
	{
		this.amount = amount;
	}
	/**
	 * 
	 */
	public Set getBars()
	{
		return bars;
	}
	public void setBars(Set bars)
	{
		this.bars = bars;
	}
	/**
	 * @hibernate.property
	 */
	public Date getDate()
	{
		return date;
	}
	public void setDate(Date date)
	{
		this.date = date;
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

	/**
	 * @hibernate.map
	 *  table="FooInfo"
	 *  lazy="true"
	 *  cascade="all"
	 * @hibernate.key
	 *  column="foo"
	 * @hibernate.index
	 *  column="name"
	 * 	type="java.lang.String"
	 *  length="80"
	 * @hibernate.element 
	 *  column="val"
	 * 	type="java.lang.String"
	 */
	public Map getProperties()
	{
		return properties;
	}
	public void setProperties(Map<String, String> properties)
	{
		this.properties = properties;
	}
	
	public String getProperty(String name)
	{
		return (String)properties.get(name);
	}
	public void setProperty(String name, String value)
	{
		this.properties.put(name, value);
	}
	/**
	 * @hibernate.many-to-one cascade="all"
	 */
	public Bar getBar()
	{
		return bar;
	}
	public void setBar(Bar bar)
	{
		this.bar = bar;
	}
	/**
	 * @hibernate.component
	 */
	public Tao getTao()
	{
		return tao;
	}
	public void setTao(Tao tao)
	{
		this.tao = tao;
	}
}
