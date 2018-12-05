/**
 * 
 * created on 2007-6-18
 */
package net.ninecube.test.domain;

import java.util.List;

import net.ninecube.core.domain.Entity;

/**
 * 
 * @hibernate.meta attribute="auto-generated"
 * @hibernate.class  table="TStudentForTest" 
 * @author jxb
 */
public class Student implements Entity {

	private Long idno;
	private String name;
	private Student peer;
	private List<Student> classmate;
	private String mainrole;
	private List<String> roles;
	
	//~ getter and setter
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
	 * @hibernate.many-to-one
	 */
	public Student getPeer() {
		return peer;
	}
	public void setPeer(Student peer) {
		this.peer = peer;
	}
	/**
	 * @hibernate.bag inverse="false" lazy="true" table="TStudentClassMates"
	 * @hibernate.key column="eid"
	 * @hibernate.many-to-many column="mid" class="net.ninecube.test.domain.Student"
	 */
	public List<Student> getClassmate() {
		return classmate;
	}
	public void setClassmate(List<Student> classmate) {
		this.classmate = classmate;
	}
	/**
	 * @hibernate.property
	 */
	public String getMainrole() {
		return mainrole;
	}
	public void setMainrole(String mainrole) {
		this.mainrole = mainrole;
	}
	/**
	 * @hibernate.bag inverse="false" lazy="true" table="TStudentRoles"
	 * @hibernate.key column="sid"
	 * @hibernate.element column="role" type="string"
	 */
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
