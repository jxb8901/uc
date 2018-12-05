/**
 * 
 * created on 2007-2-3
 */
package net.ninecube.saturn.database.impl;

import net.ninecube.saturn.database.AbstractDatabaseTestCase;

/**
 * 
 * @author jxb
 * 
 */
public class DynaDateColumnTest extends AbstractDatabaseTestCase {

	public void testGetSqlCode() {
		ColumnImpl c = (ColumnImpl) fixture.trans.getTableColumnByAlias("交易日期");
		DynaDateColumn d = new DynaDateColumn(c, "年月");
		this.assertEquals("substring(date_format(trans.transdate,%Y%m%d),1,7)", d.getSqlCode().toString());
	}
	
	public void testType(){
		DynaDateColumn.Type type = DynaDateColumn.Type.get("年月日");
		this.assertEquals(1, type.getStartIndex(false));
		this.assertEquals(8, type.getLength(false));
		this.assertEquals(1, type.getStartIndex(true));
		this.assertEquals(10, type.getLength(true));
		this.assertEquals("yyyyMMdd", type.getPattern(false));
		this.assertEquals("yyyy-MM-dd", type.getPattern(true));
		
		type = DynaDateColumn.Type.get("年月");
		this.assertEquals(1, type.getStartIndex(false));
		this.assertEquals(6, type.getLength(false));
		this.assertEquals(1, type.getStartIndex(true));
		this.assertEquals(7, type.getLength(true));
		this.assertEquals("yyyyMM", type.getPattern(false));
		this.assertEquals("yyyy-MM", type.getPattern(true));
		
		type = DynaDateColumn.Type.get("月日");
		this.assertEquals(5, type.getStartIndex(false));
		this.assertEquals(4, type.getLength(false));
		this.assertEquals(6, type.getStartIndex(true));
		this.assertEquals(5, type.getLength(true));
		this.assertEquals("MMdd", type.getPattern(false));
		this.assertEquals("MM-dd", type.getPattern(true));
		
		type = DynaDateColumn.Type.get("年");
		this.assertEquals(1, type.getStartIndex(false));
		this.assertEquals(4, type.getLength(false));
		this.assertEquals(1, type.getStartIndex(true));
		this.assertEquals(4, type.getLength(true));
		this.assertEquals("yyyy", type.getPattern(false));
		this.assertEquals("yyyy", type.getPattern(true));
		
		type = DynaDateColumn.Type.get("月");
		this.assertEquals(5, type.getStartIndex(false));
		this.assertEquals(2, type.getLength(false));
		this.assertEquals(6, type.getStartIndex(true));
		this.assertEquals(2, type.getLength(true));
		this.assertEquals("MM", type.getPattern(false));
		this.assertEquals("MM", type.getPattern(true));


		type = DynaDateColumn.Type.get("日");
		this.assertEquals(7, type.getStartIndex(false));
		this.assertEquals(2, type.getLength(false));
		this.assertEquals(9, type.getStartIndex(true));
		this.assertEquals(2, type.getLength(true));
		this.assertEquals("dd", type.getPattern(false));
		this.assertEquals("dd", type.getPattern(true));
		
		
	}

}
