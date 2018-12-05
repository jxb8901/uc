/**
 * 
 * created on 2007-4-20
 */
package net.ninecube.core.hibernatemapping;

import java.util.List;

/**
 * @hibernate.meta attribute="auto-generated"
 * @hibernate.class
 * @author jxb
 */
public class Book {

	/**
	 * @hibernate.id  generator-class="native" access="field"
	 */
	public Long id;
	/**
	 * @hibernate.property access="field"
	 */
	public String name;
	/**
	 * @hibernate.bag inverse="true" lazy="false"
	 * @hibernate.key
	 * @hibernate.many-to-many class="net.ninecube.core.hibernatemapping.Author"
	 */
	public List<Author> authors;
}
