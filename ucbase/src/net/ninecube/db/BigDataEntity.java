/**
 * 2005-8-15
 */
package net.ninecube.db;

import net.ninecube.lang.TypedMap;

/**
 * @author JXB
 *
 */
public abstract class BigDataEntity extends AbstractEntity{

	private TypedMap data;
	
	//~ getter and setter 

	public TypedMap getData() {
		if (!isNew() && data == null) {
			data = DBUtil.getBigData(getTableName(), getKeyFieldName(), getDataFieldName(), getKeyValue());
		}
		return data;
	}

	public void setData(TypedMap data) {
		this.data = data;
	}
	
	public void saveData() {
		if (data != null && !data.isEmpty()) {
			DBUtil.setBigData(getTableName(), getKeyFieldName(), getDataFieldName(), getKeyValue(), data);
		}
	}
	
	protected abstract String getTableName();
	protected abstract String getKeyFieldName();
	protected abstract String getDataFieldName();
	protected abstract String getKeyValue();
}
