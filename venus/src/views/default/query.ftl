<html>
<head>
<!-- BeginTemplate name="title" -->
<title>欢迎登录UC银行客户积分营销系统</title>
<!-- EndTemplate -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<@ww.head />
<!-- BeginTemplate name="script" -->
<#function templateExists name>
	<#assign fullname="${config.owner.package.viewPackageName}/${name}" />
	<#return stack.findValue("@net.ninecube.core.webwork.util.FreemarkerUtil@isTemplateExists('${fullname}')") >
</#function>
<#macro includeIfExists name>
	<#if templateExists(name) >
		<#assign x>
			<#include "${config.owner.package.viewPackageName}/${name}" />
		</#assign>
		<#nested x>
	</#if>
</#macro>
<@includeIfExists "query.css" ; x >
<style type="text/css"><!-- ${x} --></style>
</@includeIfExists>
<@includeIfExists "query.js" ; x >
<script  type="text/javascript"><!-- ${x} --></script>
</@includeIfExists>
<!-- EndTemplate -->
</head>
<body>
<!-- BeginTemplate name="head" --><!-- EndTemplate -->

<!-- BeginTemplate name="message" --><!-- EndTemplate -->

<!-- BeginTemplate name="content" -->
<#if config.inputViewFields?size != 0>
<@ww.form method="post" >
	<@ww.component template="forminput.ftl" />
	<@ww.div cssClass="buttons">
		<@ww.submit name="action:query" value="查询" tooltip="查询" />
	</@ww.div>
	<@ww.component template="hideparams.ftl" include="select,buttons,type"/>
</@ww.form>
</#if>

<@ww.form method="post" type="list">
	<@ww.component template="tablelist.ftl" />
	<@ww.div cssClass="buttons">
		<#if Parameters.buttons?exists >
			<@includeIfExists "${Parameters.buttons}.ftl" ; x>${x}</@includeIfExists>
		<#elseif Parameters.select?exists >
			<@ww.submit value="选择" onclick="doSelect('${Parameters.type?default('')}'); return false;" />
			<@ww.submit value="关闭" onclick="window.parent.close(); return false;" />
		<#else>
			<@ww.component template="submits.ftl" />
			<@includeIfExists "extendbuttons.ftl" ; x>${x}</@includeIfExists>
		</#if>
	</@ww.div>
	<@ww.component template="hideparams.ftl" include="select,buttons,type"/>
</@ww.form>
<!-- EndTemplate -->
</body>
</html>
