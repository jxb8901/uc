package net.ninecube.fishman

class Test1 extends GroovyTestCase {

static def template = """\
<html>
<head>
<title><!-- BeginTemplate name="title" -->默认标题<!-- EndTemplate --></title>
</head>
<body>
<table>
	<tr><!-- BeginTemplate name="head" -->页面标题<!-- EndTemplate --><td></td></tr>
	<tr><td><!-- BeginTemplate name="content" --><!-- EndTemplate --></td></tr>
</table>
</body>
</html>
"""
static def instance = """\
<html>
<head>
<title><!-- BeginTemplate name="title" --><!-- EndTemplate --></title>
</head>
<body>
<table>
	<tr><!-- BeginTemplate name="head" -->用户查询<!-- EndTemplate --><td></td></tr>
	<tr><td><!-- BeginTemplate name="content" -->
		<form></form>
		<table></table>
	<!-- EndTemplate --></td></tr>
</table>
</body>
</html>
"""
static def expected = """\
<html>
<head>
<title><!-- BeginTemplate name="title" -->默认标题<!-- EndTemplate --></title>
</head>
<body>
<table>
	<tr><!-- BeginTemplate name="head" -->用户查询<!-- EndTemplate --><td></td></tr>
	<tr><td><!-- BeginTemplate name="content" -->
		<form></form>
		<table></table>
	<!-- EndTemplate --></td></tr>
</table>
</body>
</html>
"""
	public void test() {      
		FishManTest.assertTrue(expected, template, instance);
	}

	public static void main11(String[] s) {
		FishManTest.assertTrue(expected, template, instance);
	}
}
