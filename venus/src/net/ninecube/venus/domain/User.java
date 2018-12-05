/*
 * Created on 2004-3-8
 */
package net.ninecube.venus.domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ninecube.core.domain.Entity;

/**
 * 用户
 * @author  jxb
 * @hibernate.meta attribute="auto-generated"
 * @hibernate.class  table="TUser" 
 * @hibernate.query name="getUserByName" 
 * 	query="from User where name=:name"
 */
public class User implements Entity , net.ninecube.core.security.User
{
	//~ private fields
	//id
	private Long idno;
	//姓名
	private String name;
	//密码
	private String password;
	//是否超级用户
	private boolean superuser = false;
	
	//创建时间
	private Date creationDate;
	//最近一次访问时间
	private Date lastAccessDate;
	//最近一次访问IP
	private String ip;
	
	//是否禁用
	private boolean disabled = true;
	//禁用时间
	private Date disabledDate;
	
	//角色
	private List<Role> roles;
	
	//用户属性(其它信息)
	private Map<String, String> userInfo = new HashMap<String, String>();

	//~ business method

	public boolean isGranted(String perm) {
		if(this.isSuperuser()) return true;
		for(Role r : getRoles()){
			if(r.getPermissions().contains(perm)) return true;
		}
		return false;
	}

	public boolean checkPassword(String password) {
		return this.password != null && this.password.equals(password);
	}


	//~ getter and setter
	
	/**
	 * @hibernate.id  generator-class="native"
	 */
	public Long getIdno()
	{
		return idno;
	}

	/**
	 * @param id
	 */
	public void setIdno(Long id)
	{
		this.idno = id;
	}

	/**
	 * @hibernate.property
	 */
	public Date getCreationDate()
	{
		return creationDate;
	}

	/**
	 * @param creationDate
	 */
	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}

	/**
	 * @hibernate.property
	 */
	public boolean isDisabled()
	{
		return disabled;
	}

	/**
	 * @param disabled
	 */
	public void setDisabled(boolean disabled)
	{
		this.disabled = disabled;
	}

	/**
	 * @hibernate.property
	 */
	public Date getDisabledDate()
	{
		return disabledDate;
	}

	/**
	 * @param disabledDate
	 */
	public void setDisabledDate(Date disabledDate)
	{
		this.disabledDate = disabledDate;
	}
	
	public void disabled(boolean disabled)
	{
		setDisabled(disabled);
		setDisabledDate(new Date());
	}

	/**
	 * @hibernate.property
	 */
	public String getIp()
	{
		return ip;
	}

	/**
	 * @param ip
	 */
	public void setIp(String ip)
	{
		this.ip = ip;
	}

	/**
	 * @hibernate.property
	 */
	public Date getLastAccessDate()
	{
		return lastAccessDate;
	}

	/**
	 * @param lastAccessDate
	 */
	public void setLastAccessDate(Date lastAccessDate)
	{
		this.lastAccessDate = lastAccessDate;
	}

	/**
	 * @hibernate.property
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @hibernate.property
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	/**
	 * @hibernate.set  table="TUserRoles"  lazy="false"
	 * @hibernate.key  column="userId"
	 * @hibernate.many-to-many    column="roleId"  class="net.ninecube.venus.domain.Role"
	 */
	public List<Role> getRoles()
	{
		return roles;
	}

	/**
	 * @param roles
	 */
	public void setRoles(List<Role> roles)
	{
		this.roles = roles;
	}
	
	public boolean hasRole(String role)
	{
		return this.roles.contains(role);
	}

	/**
	 * @hibernate.map  table="TUserInfo"  lazy="true"  cascade="all"
	 * @hibernate.key  column="userId"
	 * @hibernate.index  column="name"  type="java.lang.String"  length="80"
	 * @hibernate.element    column="val"  type="java.lang.String"
	 */
	public Map getUserInfo()
	{
		return userInfo;
	}

	/**
	 * @param properties
	 */
	public void setUserInfo(Map<String, String> properties)
	{
		this.userInfo = properties;
	}
	
	public String getProperty(String key)
	{
		return (String)this.userInfo.get(key);
	}
	
	public void setProperty(String key, String value)
	{
		this.userInfo.put(key, value);
	}
	
	/**
	 * @return  Returns the superuser.
	 * @hibernate.property
	 */
	public boolean isSuperuser()
	{
		return superuser;
	}

	/**
	 * @param superuser  The superuser to set.
	 */
	public void setSuperuser(boolean superuser)
	{
		this.superuser = superuser;
	}
	
	public boolean equals(Object o)
	{
		if (o instanceof User)
		{
			if (this.getIdno() == null) return false;
			return this.getIdno().equals(((User)o).getIdno());
		}
		return false;
	}
	
	public int hashCode()
	{
		if (this.getIdno() == null) return 0;
		return this.getIdno().hashCode();
	}
}
