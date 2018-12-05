/**
 * 
 * created on 2007-4-18
 */
package net.ninecube.core.hibernatemapping;

/**
 * 
 * @hibernate.meta attribute="auto-generated"
 * @hibernate.class  table="TBSource" 
 * @author jxb
 */
public class BSource implements Source {

	private Long id;
	private String name;
	private String btype;
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
	public String getBtype() {
		return btype;
	}
	public void setBtype(String btype) {
		this.btype = btype;
	}
}
