package net.ninecube.venus.domain;

import java.util.List;
import java.util.Set;

import net.ninecube.core.domain.Entity;

/**
 * 用户
 * @author  jxb
 * @hibernate.meta attribute="auto-generated"
 * @hibernate.class  table="TRole" 
 */
public class Role implements Entity, net.ninecube.core.security.Role {
	private Long idno;
	private String name;
	private String description;
	private List<String> permissions;

	/**
	 * @hibernate.set  table="TRolePermissions"  lazy="false"  cascade="all"
	 * @hibernate.key  column="RoleId"
	 * @hibernate.element    column="Permission"  type="string"
	 */
	public List<String> getPermissions(){
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	/**
	 * @hibernate.id  generator-class="native"
	 */
	public Long getIdno() {
		return idno;
	}

	public void setIdno(Long idno) {
		this.idno = idno;
	}

	/**
	 * @hibernate.property
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @hibernate.property
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
