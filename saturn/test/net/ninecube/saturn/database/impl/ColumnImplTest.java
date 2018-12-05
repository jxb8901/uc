/**
 * 
 * created on 2007-2-3
 */
package net.ninecube.saturn.database.impl;

import java.util.Map;

import net.ninecube.saturn.database.AbstractDatabaseTestCase;
import net.ninecube.saturn.database.Column;
import net.ninecube.util.EnumerManager;

/**
 * 
 * @author jxb
 * 
 */
public class ColumnImplTest extends AbstractDatabaseTestCase {

	public void testGetSqlCodeAndValue() {
		Column c = fixture.trans.getTableColumnByAlias("交易日期");
		this.assertEquals("trans.transdate", c.getSqlCode().toString());
		this.assertEquals("20060102", c.getSqlValue("20060102").toString());
	}
	
	public void testGetSqlCodeAndValueOnFormula() {
		ColumnImpl c = (ColumnImpl) fixture.trans.getTableColumnByAlias("交易月份");
		this.assertEquals("substring(transdate,   4,2)", c.getFormula());
		this.assertEquals("substring(trans.transdate,4,2)", c.getSqlCode().toString());
		this.assertEquals("12", c.getSqlValue("12").toString());
	}
	
	public void testGetSqlCodeAndValueOnEnumer() {
		Column c = fixture.trans.getTableColumnByAlias("交易类型");
		this.assertEquals("trans.transtype", c.getSqlCode().toString());
		EnumerManager.set(new EnumerManager(){
			@Override public String getValue(String schema, String alias) {
				assertEquals("transtype", schema);
				assertEquals("房地产类", alias);
				return "12";
			}
			@Override public Map<Object, String> getValues(String schema) {
				return null;
			}
		});
		((ColumnImpl)c).setEnumer("transtype"); // schema
		try {
			this.assertEquals("12", c.getSqlValue("房地产类").toString());
		} finally {
			EnumerManager.setDefault();
		}
	}
}
