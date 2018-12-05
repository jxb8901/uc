/**
 * 
 * created on 2007-4-20
 */
package net.ninecube.core.hibernatemapping;

/**
 * @hibernate.meta attribute="auto-generated"
 * @hibernate.class
 * @author jxb
 */
public class Author {

	/**
	 * @hibernate.id  generator-class="native" access="field"
	 */
	public Long id;
	/**
	 * @hibernate.property access="field"
	 */
	public String name;
}
