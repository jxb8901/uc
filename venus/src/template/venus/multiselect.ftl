<#--
多选组件，支持关联模型或是枚举数据源，支持单选（一对一）及多选（多对一）模式
支持多种展示方式：select, radio/checkbox, text

参数：
 @param name 模型字段名称，必填
 @param type input/output 指定是从input或output字段中生成
		为input时将生成一个select控件和一个可弹出选择对话框的按钮
 		为output时将仅生成文本字符串
 @param readonly true/false 是否只读
-->
<#-- 解析field对象 -->
<#if parameters.type?default("input") == "input">
	<#if parameters.name?exists>
		<#assign field=config.getInputField(parameters.name) />
	<#else>
		<#assign field=config.inputViewFields[parameters.index?number] />
	</#if>
	<#assign theme="xhtml" />
<#else>
	<#if parameters.name?exists>
		<#assign field=config.getOutputField(parameters.name) />
	<#else>
		<#assign field=config.outputViewFields[parameters.index?number] />
	</#if>
	<#assign theme="simple" />
</#if>
<#-- prepare -->
<#assign cssClass="" /><#if parameters.readonly?default('false') == 'true'><#assign cssClass="readonly" /></#if>
<#if "${field.type}" == "enumt" >
	<#assign list=field.enum />
	<#function getValue k><#return list[k]?default('') /></#function>
<#elseif "${field.type}" == "model" >
	<#assign listValue=action.join(action.ognl('#this.{name}', field.fields), "+' '+") />
	<#function getValue k><#return action.ognl(listValue, k) /></#function>
</#if>
<#-- generate -->
<#if (parameters.type?default("input") == "output" || "${field.tagtype}" == "text")>
	<#if field.multi>
		<#list stack.findValue(field.name)?default([]) as k >[${getValue(k)?default('')}]</#list>
	<#else>
		${getValue(stack.findValue(field.name)?default(''))?default('')}
	</#if>
<#else>
	<#-- prepare -->
	<#if "${field.type}" == "enumt" >
		<#assign name=field.name />
		<#if field.multi>
			<#assign value=action.ognl("#this.{''+#this}", stack.findValue(field.name)?default([]))?default([]) />
		<#else>
			<#assign value=stack.findValue("''+${name}")?default('') />
		</#if>
		<#assign listKey="''+key" />
		<#assign listValue="value" />
		<#if field.multi && "${field.tagtype}" == "select" ><#-- 过滤元素:只有多选的select时才过滤 -->
			<#assign dummy=stack.setValue("#tmpMap1", stack.findValue("#"+"{}")) />
			<#assign dummy=stack.setValue("#tmpMap2", stack.findValue("#"+"{}")) />
			<#assign firstList=stack.findValue(field.name)?default([]) />
			<#list list?keys as key>
				<#if firstList?seq_contains(key) >
					<#assign dummy=stack.setValue("#tmpMap2['${key}']", list[key]) />
				<#else>
					<#assign dummy=stack.setValue("#tmpMap1['${key}']", list[key]) />
				</#if>
			</#list>
			<#assign list=stack.findValue("#tmpMap1") />
			<#assign firstList=stack.findValue("#tmpMap2") />
		</#if>
	<#elseif "${field.type}" == "model" >
		<#assign em=action.getBean(field.model.managerName) />
		<#assign name=field.name+"."+field.model.key.name />
		<#assign list=em.find(1, 30).records />
		<#if field.multi>
			<#assign value=action.ognl("#this.{''+${field.model.key.name}}", stack.findValue(field.name)?default([]))?default([]) />
		<#else>
			<#assign value=stack.findValue("''+${name}")?default('') />
		</#if>
		<#assign listKey="''+${field.model.key.name}" />
		<#assign firstList=stack.findValue(field.name)?default([]) />
		<#if field.multi && "${field.tagtype}" == "select" ><#-- 过滤元素:只有多选的select时才过滤 -->
			<#assign dummy=stack.setValue("#tmpList1", list) />
			<#assign dummy=stack.setValue("#tmpList2", firstList) />
			<#assign list=stack.findValue("#tmpList1.{?#this not in #tmpList2}") />
		</#if>
	</#if>
	<#-- generate -->
	<#if "${field.tagtype}" == "checkbox" && field.multi ><#-- 多选 checkbox -->
		<@ww.checkboxlist label="${field.cname}" name=name list=list value=value cssClass="${cssClass}" 
			disabled="${parameters.readonly?default(false)}" listKey="${listKey}" listValue="${listValue}"/>
	<#elseif "${field.tagtype}" == "radio" && field.multi ><#-- 单选 radio -->
		<@ww.radio label="${field.cname}" name=name list=list value=value cssClass="${cssClass}" 
			 disabled="${parameters.readonly?default(false)}"listKey="${listKey}" listValue="${listValue}"/>
	<#else><#-- 默认为select -->
		<#include "selectmodel.ftl" />
	</#if>
</#if>