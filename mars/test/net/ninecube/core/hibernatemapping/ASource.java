/**
 * 
 * created on 2007-4-18
 */
package net.ninecube.core.hibernatemapping;

/**
 * 
 * @hibernate.meta attribute="auto-generated"
 * @hibernate.class  table="TASource" 
 * @author jxb
 */
public class ASource implements Source {

	private Long id;
	private String name;
	private String atype;
	/**
	 * @hibernate.id  generator-class="native"
	 */
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getAtype() {
		return atype;
	}
	public void setAtype(String atype) {
		this.atype = atype;
	}
}
