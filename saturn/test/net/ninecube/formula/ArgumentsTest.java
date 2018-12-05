package net.ninecube.formula;

import java.util.List;

import junit.framework.TestCase;

public class ArgumentsTest extends TestCase {
	
	public void testUnnameArgumentValueIterate() {
		Arguments args = new Arguments();
		args.setArgument(0, "0");
		args.setArgument("test", "test");
		args.setArgument(1, "1");
		List t = args.unnamedArgumentValueIterate();
		this.assertEquals(2, t.size());
		this.assertEquals("1", t.get(0)); // 不能保证顺序
		this.assertEquals("0", t.get(1));
	}

}
