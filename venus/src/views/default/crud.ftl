<html>
<head>
<!-- BeginTemplate name="title" -->
<title>欢迎登录UC银行客户积分营销系统</title>
<!-- EndTemplate -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<@ww.head />
<!-- BeginTemplate name="script" -->
<#macro includeIfExists name>
	<#if stack.findValue("@net.ninecube.core.webwork.util.FreemarkerUtil@isTemplateExists('${name}')") >
		<#assign x>
			<#include "${name}" />
		</#assign>
		<#nested x>
	</#if>
</#macro>
<@includeIfExists "${config.owner.package.viewPackageName}/crud.css" ; x >
<style type="text/css"><!-- ${x} --></style>
</@includeIfExists>
<@includeIfExists "${config.owner.package.viewPackageName}/crud.js" ; x >
<script  type="text/javascript"><!-- ${x} --></script>
</@includeIfExists>
<!-- EndTemplate -->
</head>
<body>
<!-- BeginTemplate name="head" --><!-- EndTemplate -->

<!-- BeginTemplate name="message" --><!-- EndTemplate -->

<!-- BeginTemplate name="content" -->
<@ww.form method="post">
	<@ww.component template="forminput.ftl" />
	<@ww.div cssClass="buttons">
		<@ww.if test="''+config.owner.type != 'read'"><@ww.submit name="method:submit" value="确定" /></@ww.if>
		<@ww.submit name="method:back" value="返回" />
	</@ww.div>
	<@ww.component template="hideparams.ftl" fromparam="true" />
</@ww.form>
<!-- EndTemplate -->
</body>
</html>
