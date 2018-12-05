package net.ninecube.saturn;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.saturn.ast.RuleParser;
import net.ninecube.saturn.ast.SimpleNode;
import net.ninecube.saturn.database.DatabaseManager;
import net.ninecube.util.FileUtil;
import net.ninecube.util.StringUtil;

import org.apache.log4j.Logger;

public class RuleFileExecuteTest extends RuleFileTest {	
	protected static Logger log = Logger.getLogger(RuleFileExecuteTest.class);
	protected String TEST_SINGAL_FILE = "-1.1";
	private RuleEngine re;
	private URL url;
	private TestData input;

	public void testExecute() throws Exception {
		List<FrequenceDate> freqdates = new ArrayList<FrequenceDate>();
		FrequenceDate freq = new FrequenceDate(input.getTargetDate());
		freqdates.add(freq);
		log.debug("rule variables : " + input.getVariables());
		re.execute(freqdates ,input.getFilter() , re.createRule(FileUtil.getFileContent(
				new InputStreamReader(RuleFileExecuteTest.class.getResourceAsStream(
						RULE_FILE_DIR + input.getRule()), ENCODING)),""),input.getVariables());
		new SqlResultCheck(input.getResult()).check();
	}
	public RuleFileExecuteTest(URL url) throws Exception {
		super("testExecute");
		this.url = url;
	}
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		re = RuleEngine.getInstance();
		this.input = readBlocks(new InputStreamReader(url.openStream(), ENCODING));
		super.setup.executeSql(this.input.getSetupSql());
	}
	
	@Override 
	public Test getTest(URL url) throws Exception {
		return new RuleFileExecuteTest(url);
	}
	@Override public String getTestSingalFile() { return this.TEST_SINGAL_FILE; }

	public static Test suite() throws Exception {
		return new RuleFileExecuteTest(null).getSuite("testcase");
	}
}
