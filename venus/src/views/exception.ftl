<html>
<head>
<!-- BeginTemplate name="title" -->
<title>欢迎登录UC银行客户积分营销系统</title>
<!-- EndTemplate -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<@ww.head />
<!-- BeginTemplate name="script" --><!-- EndTemplate -->
</head>
<body>
<!-- BeginTemplate name="head" --><!-- EndTemplate -->

<!-- BeginTemplate name="message" --><!-- EndTemplate -->

<!-- BeginTemplate name="content" -->
<div class="error">操作出错！${exception?default("")}</div>
<@ww.form method="post">
	<@ww.div cssClass="buttons">
		<@ww.submit name="method:back" value="返回" />
	</@ww.div>
	<@ww.component template="hideparams.ftl" />
</@ww.form>
<div class="stacktrace" style="display:block; padding-left: 2em">
<pre>${exceptionStack?default("")}</pre>
</div>
<!-- EndTemplate -->
</body>
</html>
