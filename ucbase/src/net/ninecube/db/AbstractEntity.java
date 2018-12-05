/**
 * 2005-8-15
 */
package net.ninecube.db;

/**
 * @author JXB
 *
 */
public abstract class AbstractEntity implements Entity{

	private boolean isNew = true;
	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) { 
			return null;
		}
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	
}
