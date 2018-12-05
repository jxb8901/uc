package net.ninecube.saturn.ast;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

public class RuleParserTest extends TestCase {
	
	RuleParser parser=null;
	private Logger log = Logger.getLogger(this.getClass());
	protected void setUp() throws Exception {
		parser=new RuleParser("");
		 
	}
	
	

	public void testParseRelationExpr()throws Exception {
		String sampleRule="c = (a or b) and (c or d)";
		String expected = "Start\r\n"
		+ " Import\r\n"
		+ "  Name\r\n"
		+ " Expression\r\n"
		+ "  RelationalExpression\r\n"
		+ "   PrimaryPrefix\r\n"
		+ "    Name\r\n"
		+ "   PrimaryPrefix\r\n"
		+ "    Name\r\n";
		SimpleNode node=new RuleParser(sampleRule).parse(null);
		assertNotNull(node);

		ByteArrayOutputStream tmpOut=new ByteArrayOutputStream(); 
		PrintStream out=System.out;
		System.setOut(new PrintStream(tmpOut));
		node.dump("");
		System.setOut(out);
		String realOut=tmpOut.toString();
		//System.out.print(realOut);
		//assertEquals(expected,realOut );
		
	}
	
	public void testChineseSyntax() throws Exception {
		assertTrueSyntax("客户.性别 等于 '女'");
		assertTrueSyntax("如果 客户.性别 == '女' 并且 客户.出生日期.月份 == '12'\r\n 客户.积分 += 100");
	}
	
	public void testVariableSyntax() throws Exception {
		assertTrueSyntax("如果 客户.性别 == '女' 并且 客户.出生日期.月份 == {出生日期}\r\n 客户.积分 += {送积分}");
	}
	
	public void testIgnoreCase() throws Exception {
		assertTrueSyntax("if 客户.性别 == '女' and 客户.出生日期.月份 == 12\r\n 客户.积分 += 10");
		assertTrueSyntax("If 客户.性别 == '女' anD 客户.出生日期.月份 == 12\r\n 客户.积分 += 10");
		assertTrueSyntax("IF 客户.性别 == '女' AND 客户.出生日期.月份 == 12\r\n 客户.积分 += 10");
	}
	
	public void testRegularRule() throws Exception {
		assertTrueSyntax("[regularRule]\r\n客户.积分 += 100");
		assertTrueSyntax("/* comment */\r\n[regularRule]\r\n客户.积分 += 100");
		assertTrueSyntax("\r\n[regularRule]\r\n客户.积分 += 100");
	}
	
	public void testSyntax() throws Exception {
		assertTrueSyntax("a = b + c \r\n");
		assertFalseSyntax("积分 > 12\r\n");
//		assertTrueSyntax("积分 = 12\r\n\r\n积分 = 18");
//		assertTrueSyntax("积分 等于 12");
//		assertTrueSyntax("积分 = 12");
//		assertTrueSyntax("\r\n");
//		//assertTrueSyntax("\r\nasd\r\n");
//		assertFalseSyntax("a or b and c or d ", "必须是语句");
//		assertTrueSyntax("c = a or b and c or d ");
//		assertTrueSyntax("c = (a or b) and (c or d)");
//		
//		assertFalseSyntax("b=a+3(d+2) ", "3后不能有括号");
//		
//		assertTrueSyntax("a=1/*注释*/+2", "支持多行注释");
//		assertTrueSyntax("a=1/*多行\r\n注释*/+2", "支持多行注释");
//		assertTrueSyntax("a=1+2//单行注释\r\n", "支持单行注释");
//		assertTrueSyntax("switch {\r\n case 1==1: \r\n a+=1+2//单行注释\r\n} \r\n", "支持单行注释");
//		//TODO:assertTrueSyntax("a=1+2//单行注释", "支持单行注释");
//		
//		assertTrueSyntax("\r\n 积分 = 12\r\n", "允许空行");
//		assertTrueSyntax("\r\n 积分 = 12\r\n\r\n积分 += 12", "允许空行");	
//		assertTrueSyntax("\r\n {积分 = 12\r\n\r\n积分 += 12\r\n}", "允许BlockStatement结束");		
		
	}

	private void assertFalseSyntax(String rule) {
		assertFalseSyntax(rule, "");
	}
	
	private void assertFalseSyntax(String rule, String msg) {
		try {
			SimpleNode node = new RuleParser(rule).parse(null);
			fail(msg);
			node.dump("");
		} catch (Exception e) {
		}
	}
	
	private void assertTrueSyntax(String rule) throws Exception {
		assertTrueSyntax(rule,"");
	}
	private void assertTrueSyntax(String rule,String msg) throws Exception {
		log .debug("rule : " + rule);
		SimpleNode node = new RuleParser(rule).parse(null);
		node.dump("");
	}
	
	public void testParseDump()throws Exception {
		String sampleRule="c = (a or b) and (c or d)";
		SimpleNode node=new RuleParser(sampleRule).parse(null);
		assertNotNull(node);

		node.dump("");
		
	}
	

}
