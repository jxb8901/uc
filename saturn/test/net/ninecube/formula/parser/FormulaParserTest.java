/**
 * 
 * created on 2007-3-18
 */
package net.ninecube.formula.parser;

import java.math.BigDecimal;

import junit.framework.TestCase;
import net.ninecube.formula.Context;
import net.ninecube.formula.exception.FormulaSyntaxException;

/**
 * 
 * @author jxb
 * 
 */
public class FormulaParserTest extends TestCase {

	private FormulaParser parser;
	private Context context;
	
	protected void setUp() {
		parser = new FormulaParser();
		context = new Context();
	}
	
	public void testArith() {
		this.assertEquals(5, parse("1 + 2 +8 / 4").eval(context).getValue().intValue());
		this.assertEquals(new BigDecimal(3), parse("1+2").eval(context).getValue());
		this.assertEquals(new BigDecimal("1.02"), parse( "1+2%").eval(context).getValue());
		this.assertEquals(new BigDecimal("0"), parse("1+2-3").eval(context).getValue());

		this.assertEquals(new BigDecimal("3.2"), parse("1+2*1.1").eval(context).getValue());
		this.assertEquals(new BigDecimal("0.03"), parse("(1+2)*1%").eval(context).getValue());
		//@ TODO: BigDecimal的精度问题？
		this.assertEquals(new BigDecimal("4.5000000000"), parse("(1+2)*1*3/2").eval(context).getValue());
	}

	public void testEmptyFormula() throws Exception {
		try {
			parse("");
			this.fail("公式不能为空");
		} catch (FormulaSyntaxException e) {
		}
	}

	public void testUnicodeFormula() throws Exception {
		assertSyntax(true, "(帐面利润+消化包袱-关注贷款余额) * 1% / 12");
		//assertSyntax(true, "(a - b +/* comment */ c)*1%");
		//assertSyntax(true, "(中国 + b - MF_Avg(tableName:O_TEST, fieldName:test))*1");
		//assertSyntax(true, "(中国 + b + c)*1*2*3");
		
		//assertSyntax(true, "(中国 + b + c) * a");
	}
	
	public void testFunction() {
		assertSyntax(true, "贷款余额(ABC,五级分类:关注贷款,科目:1000)");
		assertSyntax(true, "POS消费额(客户.性别:女) + 10 / 2");
		assertSyntax(false, "1(五级分类:关注贷款)");
	}
	
	public void testProperties() {
		String formula = "贷款余额(ABC,五级分类:关注贷款,科目:1000){频度:M}";
		assertSyntax(true, formula);
		ASTFormula f = parse(formula);
		this.assertNotNull(f);
		this.assertEquals("M", f.getProperties().get("频度"));
	}
	
	public void testIllegalToken() {
		assertSyntax(false, "贷款余额×5%");
	}
	
	private void assertSyntax(boolean valid, String formula) {
		try {
			parse(formula);
			if (!valid) fail();
		} catch (FormulaSyntaxException e) {
			//e.printStackTrace();
			if (valid) throw e;
		}
	}

	private ASTFormula parse(String s) {
		parser.ReInit(s);
		try {
			ASTFormula n = (ASTFormula)parser.parse();
			n.dump("DEBUG:");
			return n;
		} catch (ParseException e) {
			throw new FormulaSyntaxException("syntax error, formula["+s+"]", e);
		} catch (TokenMgrError e) {
			throw new FormulaSyntaxException("Illegal token, formula["+s+"]", e);
		}
	}
}
