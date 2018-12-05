/**
 * 
 * created on 2007-4-18
 */
package net.ninecube.core.hibernatemapping;

/**
 * 
 * @hibernate.meta attribute="auto-generated"
 * @hibernate.class  table="TLog" 
 * @author jxb
 */
public class Log {

	private Long id;
	private String log;
	private Source source;

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
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	/**
	 * @hibernate.any id-type="long" meta-type="string"
	 * @hibernate.meta-value value="A" class="net.ninecube.core.hibernatemapping.ASource"
	 * @hibernate.meta-value value="B" class="net.ninecube.core.hibernatemapping.BSource"
	 * @hibernate.meta-value value="C" class="net.ninecube.core.hibernatemapping.CSource"
	 * @hibernate.any-column name="tablename"
	 * @hibernate.any-column name="sourceid"
	 */
	public Source getSource() {
		return source;
	}
	public void setSource(Source source) {
		this.source = source;
	}
}
