<#--
 将当前请求中的参数全部生成为隐藏字段，以便交易携带状态信息。
 主要用于“智能返回”或“查询分页”功能。
1、默认情况下，以下字段不生成：
带action:,method:前缀的参数
密码参数：以Password开头的参数

2、
returnAction:
returnMethod:

3、标签参数说明：
 @param include 指定要生成隐藏字段的参数名称
 @param fromparam [true/false] 默认为false, 根据参数中名称为“_HP_”的值取得需要生成参数的列表
 @param inputfields [true/false] 默认为false, 指定是否生成交易的inputfields字段列表中的参数
 @param all [true/false] 默认为false, 指定是否生成所有参数
	以上参数可以指定前缀，不同的前缀表示不同的含义：
	无：生成隐藏域，不生成_HP_参数
	@：不生成隐藏域，生成_HP_参数
	$：生成隐藏域，生成_HP_参数
-->
<#assign names="" />
<#function genname x y>
	<#if x?default("")?starts_with("@") || x?default("")?starts_with("$")>
		<#assign names=names+",${y}" />
	</#if>
	<#return ! x?default("")?starts_with("@")>
</#function>
<#function acceptableName x>
	<#-- 参数值为空时不生成 -->
	<#if Parameters[x]?default("")?trim?length == 0 >
		<#return false >
	<#-- 参数名称为“_HP_”时不生成 -->
	<#elseif x == "_HP_" >
		<#return false >
	<#-- 参数名称中有“:=,#”字符时不生成 -->
	<#elseif x?contains(":") || x?contains("=") || x?contains(",") || x?contains("#") >
		<#return false >
	</#if>
	<#-- include参数中指定的参数要生成 -->
	<#if parameters.include?default("")?contains(x) >
		<#return genname(parameters.include, x) />
	</#if>
	<#-- fromparam为真且_HP_参数的值中包括了的参数要生成 -->
	<#if parameters.fromparam?default("false")?ends_with("true") >
		<#if Parameters["_HP_"]?default("")?contains(x) >
			<#return genname(parameters.fromparam, x) />
		</#if>
	</#if>
	<#-- inputfields为真且交易的inputViewFields中包括了的参数要生成 -->
	<#if parameters.inputfields?default("false")?ends_with("true") >
		<#list config.inputViewFields as field >
			<#if field.name == x>
				<#return genname(parameters.inputfields, x) />
			</#if>
		</#list>
	</#if>
	<#-- all为真，则所有参数都生成 -->
	<#if parameters.all?default("false")?ends_with("true") >
		<#return genname(parameters.all, x) />
	</#if>
	<#-- 其它参数都生成 -->
	<#return false >
</#function>
<#list Parameters?keys as item>
	<#if acceptableName(item)>
		<#lt/><input type="hidden" name="${item}" value="${Parameters[item]}" />
	</#if>
</#list>
<#if (names?length > 0) >
	<#lt/><input type="hidden" name="_HP_" value="${names}" />
</#if>