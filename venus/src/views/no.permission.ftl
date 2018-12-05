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
<div class="error">没有权限</div>
<@ww.form method="post">
	<@ww.div cssClass="buttons">
		<@ww.submit name="method:back" value="返回" />
		<@ww.submit name="action:/system/login" value="重新登录" />
	</@ww.div>
	<@ww.component template="hideparams.ftl" />
</@ww.form>
<!-- EndTemplate -->
</body>
</html>
