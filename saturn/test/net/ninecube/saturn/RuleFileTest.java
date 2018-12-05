package net.ninecube.saturn;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.ninecube.formula.Config;
import net.ninecube.formula.TestTargetManager;
import net.ninecube.formula.impl.DefaultTarget;
import net.ninecube.formula.impl.MapFormulaResolver;
import net.ninecube.formula.ruleadapter.MetricFunctionProviderImpl;
import net.ninecube.saturn.function.FunctionProvider;
import net.ninecube.saturn.function.impl.FunctionProviderImpl;
import net.ninecube.test.SetupDatabase;
import net.ninecube.util.StringUtil;

import org.apache.log4j.Logger;

public abstract class RuleFileTest extends RuleBaseTestCase {
	protected static Logger log = Logger.getLogger(RuleFileTest.class);
	protected String RULE_FILE_DIR = "/net/ninecube/saturn/testrule/";
	protected String ENCODING = "UTF-8";
	protected int FILE_COUNT_MAJOR = 30;
	protected int FILE_COUNT_MINOR = 10;
	private FunctionProvider orgProvider;

	//~

	
	public RuleFileTest(String name) {
		super(name);
	}
	
	public Test getTest(URL url) throws Exception {
		return null;
	}
	
	public String getTestSingalFile() { return null; }

	public Test getSuite(String filePrefix) throws Exception {
		List<URL> files = listParserTests(filePrefix);
		TestSuite suite = new TestSuite(this.getClass().getName());
		for (URL url : files) {
			TestSuite filesuite = new TestSuite(url.toString().replaceAll("[^/\\\\]*[/\\\\]", ""));
			filesuite.addTest(this.getTest(url));
			suite.addTest(filesuite);
		}
		return suite;
	}

	public List<URL> listParserTests(String namePrefix) throws IOException {
		List<URL> ret = new ArrayList<URL>();
		if (!StringUtil.isEmpty(this.getTestSingalFile()) && !this.getTestSingalFile().startsWith("-")) {
			addTestFile(ret, namePrefix, this.getTestSingalFile());
		}
		else for (int i = 0; i < FILE_COUNT_MAJOR; i++) {
			addTestFile(ret, namePrefix, "" + i);
			for (int j = 0; j < FILE_COUNT_MINOR; j++) {
				addTestFile(ret, namePrefix, i +"." + j);
			}
		}
		if (ret.isEmpty()) throw new RuntimeException("没有找到任何测试文件");
		return ret;
    }
	
	private void addTestFile(List<URL> files, String namePrefix, String suffix) {
		String filename = RULE_FILE_DIR + namePrefix + "." + suffix + ".txt";
		URL is = RuleFileTest.class.getResource(filename);
		if (is == null || StringUtil.isEmpty(is.toString())) return;
		files.add(is);
	}

    protected TestData readBlocks(Reader input) throws IOException {
    	TestData ret = new TestData();
        LineNumberReader reader = new LineNumberReader(input);
        String line;
        String blockName = "=Input";
        while ((line = reader.readLine()) != null) {
            if (line.trim().startsWith("~~~") && line.trim().endsWith("~~~")) {
                blockName = line.substring(4, line.length() - 4).trim();
            } else {
            	process(ret, blockName, line);
            }
        }
        return ret;
    }
    
    protected void process(TestData data, String name, String content) {
    	if (name.startsWith("=")) {
    		name = name.substring(1);
    		int i = content.indexOf("=");
    		if (i >= 0) {
    			name = name + "." + content.substring(0, i);
    			content = content.substring(i + 1);
    		}
    		name = name.trim();
        	data.blocks.put(name, content);
    	}
    	else {
    		content = content + "\n";
    		if (data.blocks.containsKey(name))
    			content = data.blocks.get(name) + content;
        	data.blocks.put(name, content);
    	}
    }
    
    public static class TestData {
	private Map<String, String> blocks = new HashMap<String, String>();
	public String getRule() { return blocks.get("Input.Rule"); }
	public String getFilter() { return blocks.get("Input.Filter"); }
	public String getTargetDate() { return blocks.get("Input.TargetDate"); }
	public String getResult() { return blocks.get("Result"); }
	public List<String> getSetupSql() {
		try {
			if (blocks.containsKey("SetupSql"))
				return SetupDatabase.split2Sqls(blocks.get("SetupSql"));
		} catch (IOException e) {
		} 
		return Collections.emptyList();
	}
	public Map<String, String> getVariables(){
		  Map<String, String> r = new HashMap<String, String>();
		 for(Iterator<String> it = blocks.keySet().iterator(); it.hasNext() ; ){
			 String okey = it.next();
			 if(okey.startsWith("Variables.")){
				 r.put(okey.substring("Variables.".length()), blocks.get(okey));
			 }
		 }
		  return r;
	}
   }
    
    public static void main(String[] s) throws Exception {
    	System.out.println(new RuleFileExecuteTest(null).TEST_SINGAL_FILE);
   }
}
