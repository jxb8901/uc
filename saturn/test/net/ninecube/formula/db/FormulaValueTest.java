/**
 * 
 * created on 2007-6-28
 */
package net.ninecube.formula.db;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jrc.contribute.Jrc;

import net.ninecube.db.Sql;
import net.ninecube.lang.Frequence;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.saturn.RuleEngine;
import net.ninecube.saturn.database.DataSet;
import net.ninecube.saturn.database.DatabaseManager;
import net.ninecube.saturn.database.TableImplTestFixture;
import net.ninecube.saturn.impl.RuleEngineImpl;
import junit.framework.TestCase;

/**
 * 
 * @author jxb
 */
public class FormulaValueTest extends TestCase {
	protected TableImplTestFixture fixture = new TableImplTestFixture();
	private FormulaValue fv;
	private DataSet ds;
	
	protected void setUp() throws Exception {
		super.setUp();
		fixture.setUp();
		
		fv = new FormulaValue(fixture.dbm);
		fv.setFrequenceDate(new FrequenceDate("D20070303"));
		fv.setDimensionType("dim");
		fv.setDimensionValue("1");
		
		ds = fixture.dbm.newDataSet();
	}

	public void testSelect() {
		DataSet sql = fv.selectAsDataSet();
		this.assertNotNull(sql);
		this.assertEquals("select formulavalues.formula_value from formulavalues "+
				"where (((formulavalues.frequence=D and formulavalues.date=2007-03-03) "+
				"and formulavalues.dimension_type=dim) and formulavalues.dimension=1)", 
				sql.getRelation().toString());
		fv.setDefaultDimension();
		sql = fv.selectAsDataSet();
		this.assertEquals("select formulavalues.formula_value from formulavalues "+
				"where (((formulavalues.frequence=D and formulavalues.date=2007-03-03) "+
				"and formulavalues.dimension_type=*) and formulavalues.dimension=*)", 
				sql.getRelation().toString());
	}
	
	public void testInsertValue() {
		fv.setValue(new BigDecimal("12"));
		Sql sql = fv.insert();
		this.assertNotNull(sql);
		this.assertEquals("insert into formulavalues(frequence,date,dimension_type,dimension," +
				"formula_value) values('D','2007-03-03','dim','1',12)", sql.getSql());
	}
	
	public void testInsertSelectValue() {
		ds = ds.where(Jrc.getParser().parsePredicate("trans.type='1'"));
		fv.setBase(ds);
		fv.setValue(Jrc.getParser().parseExpression("count(*)"));
		DataSet sql = fv.insertAsDataSet();
		this.assertNotNull(sql);
		this.assertEquals("insert into formulavalues(formulavalues.frequence,formulavalues.date," +
				"formulavalues.dimension_type,formulavalues.dimension," +
				"formulavalues.formula_value) select D,2007-03-03,dim,1,count (*) " +
				"from trans where trans.type=1", sql.toString());
	}
}
