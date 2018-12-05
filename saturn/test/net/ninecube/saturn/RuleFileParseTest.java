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

public class RuleFileParseTest extends RuleFileTest {
	protected String TEST_SINGAL_FILE = "-12";
	private URL url;
	
	public RuleFileParseTest(URL url) { 
		super("testSyntax"); 
		this.url = url; 
	}
	
	private String getRule() throws Exception { 
		return FileUtil.getFileContent(new InputStreamReader(url.openStream(), ENCODING)); 
	}
	
	public void testSyntax() throws Exception {
		new RuleParser(getRule()).parse(null).dump("");
	}
	
	@Override
	public Test getTest(URL url) {
		return new RuleFileParseTest(url);
	}
	@Override
	public String getTestSingalFile() { return this.TEST_SINGAL_FILE; }

	public static Test suite() throws Exception {
		return new RuleFileParseTest(null).getSuite("rule");
	}
}
