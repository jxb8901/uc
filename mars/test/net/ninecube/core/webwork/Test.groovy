package net.ninecube.core.webwork


class Test extends GroovyTestCase {
	
	
	void testSomething() {
		def x = 2
		def t = {y -> x * y}
		assert 6 == t(3)
		assertScript("assert 6 == 2 * 3")
		assertScript("assert 6 == ${t(3)}")
		[
		  "assert 4 == t(2)",
		  "assert 6 == t(3)",
		  "assert 8 == t(4)",
		 ].each { s -> 
			assertScript(s)
		}
	}
	
}
