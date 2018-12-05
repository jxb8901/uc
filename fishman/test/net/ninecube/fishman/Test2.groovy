package net.ninecube.fishman

class Test2 extends GroovyTestCase {
    
public static String template = """\
<html>
<#--
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
--> 
<head>
<!-- BeginTemplate name="title" --><title>欢迎登录积分营销系统</title><!-- EndTemplate -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="{request.contextPath}/css/main.css" rel="stylesheet" type="text/css">
<!-- BeginTemplate name="sciript" --><!-- EndTemplate -->
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
	<td height="100%" valign="top" class="container1">
		<div class="heading"><!-- BeginTemplate name="head" -->{config.cname}<!-- EndTemplate --></div>
		<div class="message"><!-- BeginTemplate name="message" -->{config.cname}<!-- EndTemplate --></div>
		<!-- BeginTemplate name="content" --><!-- EndTemplate -->
	</td>
</tr>
<tr>
	<td height="50" align="center" style="font-size:10px">&copy; All RIGHTS RESERVED. 版权所有 2005　 地址:香港路43号　电话:123456</td>
</tr>
</table>
</body>
</body>
</html>
""";
public static String instance = """\
<html>
<head>
<!-- BeginTemplate name="title" --><!-- EndTemplate -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- BeginTemplate name="sciript" --><!-- EndTemplate -->
</head>
<body>
<!-- BeginTemplate name="head" --><!-- EndTemplate -->

<!-- BeginTemplate name="message" --><!-- EndTemplate -->

<!-- BeginTemplate name="content" -->
<@ww.actionmessage />
<@ww.form>
	<@ww.component template="forminput.ftl" />
	<@ww.submit name="action:search" value="查询" />
	</@ww.form>
<@ww.form action="test" type="list">
	<@ww.component template="table.ftl" />
	<@ww.component template="pagination.ftl" action="search.action" />
	<@ww.submit name="action:edit" value="修改" />
	<@ww.submit name="action:del" value="删除" />
	<@ww.submit value="统计客户情况" onclick='return false;' />
</@ww.form>
<!-- EndTemplate -->
</body>
</html>
""";
public static String expected = """\
<html>
<#--
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
--> 
<head>
<!-- BeginTemplate name="title" --><title>欢迎登录积分营销系统</title><!-- EndTemplate -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="{request.contextPath}/css/main.css" rel="stylesheet" type="text/css">
<!-- BeginTemplate name="sciript" --><!-- EndTemplate -->
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
	<td height="100%" valign="top" class="container1">
		<div class="heading"><!-- BeginTemplate name="head" -->{config.cname}<!-- EndTemplate --></div>
		<div class="message"><!-- BeginTemplate name="message" -->{config.cname}<!-- EndTemplate --></div>
		<!-- BeginTemplate name="content" -->
<@ww.actionmessage />
<@ww.form>
	<@ww.component template="forminput.ftl" />
	<@ww.submit name="action:search" value="查询" />
	</@ww.form>
<@ww.form action="test" type="list">
	<@ww.component template="table.ftl" />
	<@ww.component template="pagination.ftl" action="search.action" />
	<@ww.submit name="action:edit" value="修改" />
	<@ww.submit name="action:del" value="删除" />
	<@ww.submit value="统计客户情况" onclick='return false;' />
</@ww.form>
<!-- EndTemplate -->
	</td>
</tr>
<tr>
	<td height="50" align="center" style="font-size:10px">&copy; All RIGHTS RESERVED. 版权所有 2005　 地址:香港路43号　电话:123456</td>
</tr>
</table>
</body>
</body>
</html>
""";
	public void test() {  
		FishManTest.assertTrue(expected, template, instance);
	}

	public static void main(String[] s) {
		FishManTest.assertTrue(expected, template, instance);
	}
}
