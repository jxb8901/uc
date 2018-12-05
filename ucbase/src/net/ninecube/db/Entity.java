/**
 * 2005-8-15
 */
package net.ninecube.db;

/**
 * @author JXB
 *
 */
public interface Entity extends Cloneable{
	
	public boolean isNew();

	public void setNew(boolean isNew);
}
