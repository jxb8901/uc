<#--
根据名称生成页面控件 

参数：
	@param name 要生成字段的名称
	@param index 要生成字段的索引，如果name属性为空，index属性才起作用
	@param type input/output 指定是从input或output字段中生成
						默认为生成输入字段
	@param readonly [true/auto] 指定页面控件是否只读，默认为auto
			true 只读
			auto 自动确定，确定的方法：如果当前交易类型是read/delete，则只读，否则不只读
-->
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
<#assign readonly="false" />
<#if parameters.readonly?default("auto") == "true">
	<#assign readonly="true" >
<#elseif parameters.readonly?default("auto") == "auto">
	<#if ["read", "delete"]?seq_contains("${config.owner.type}") >
		<#assign readonly="true" >
	</#if>
</#if>
<#assign cssClass="" /><#if readonly?default('false') == 'true'><#assign cssClass="readonly" /></#if>

<#if "${field.type}" == "enumt" || "${field.type}" == "model" >
	<@ww.component template="multiselect.ftl" name="${field.name}" index="${parameters.index?number}" 
			type="${parameters.type?default('input')}" readonly="${readonly}" />
<#else>
	<#switch "${field.tagtype}" >
		<#case "hidden">
			<@ww.hidden label="${field.cname}" name="${field.name}" theme="${theme}"/><#break>
		<#case "pre">
			<@ww.component template="pre.ftl" label="${field.cname}" name="${field.name}" 
				type="${parameters.type?default('input')}"/><#break>
		<#case "file">
			<@ww.file label="${field.cname}" name="${field.name}" readonly="${readonly}" cssClass="${cssClass}" theme="${theme}" /><#break>
		<#case "img">
			<@ww.component template="img.ftl" label="${field.cname}" name="${field.name}" type="${parameters.type?default('input')}">
				<@ww.param name="src">
					<@ww.url action="download" >
						<@ww.param name="${config.keyFieldName}"><@ww.property value="${config.keyFieldName}" /></@ww.param>
						<@ww.param name="name">${field.name}</@ww.param>
					</@ww.url>
				</@ww.param>
			</@ww.component>
			<#break>
		<#case "textarea">
			<@ww.textarea label="${field.cname}" name="${field.name}" readonly="${readonly}" 
				theme="${theme}" wrap="off" cols="30" rows="5" cssClass="${cssClass}" /><#break>
		<#case "text">
			<@ww.label label="${field.cname}" name="${field.name}" readonly="${readonly}" 
				theme="${theme}"/><#break>
		<#case "checkbox">
			<#assign fieldValue=stack.findValue(field.name) />
			<#if parameters[field.name]?default("") == "${fieldValue}"><#assign checked="true">
				<#else><#assign checked="false"></#if>
			<@ww.checkbox label="${field.cname}" name="${field.name}" fieldValue="${fieldValue}" 
				value="${checked}" readonly="${readonly}" theme="${theme}" cssClass="${cssClass}"/><#break>
		<#case "radio">
			<#assign fieldValue=stack.findValue(field.name) />
			<#assign checkedvalue=Parameters[field.name]?default("") />
			<@ww.radio name="${field.name}" list="'${fieldValue}'" listValue="''" cssClass="${cssClass}" 
				value="'${checkedvalue}'" readonly="${readonly}" theme="${theme}"/><#break>
		<#default> <#-- tagtype == auto -->
			<#switch field.type?default("string") >
				<#case "date">
					<#if (readonly == "true") >
						<@ww.datepicker label="${field.cname}" name="${field.name}" size="10" cssClass="${cssClass}" 
							format="%Y%m%d" readonly="${readonly}" theme="${theme}" /><#break>
					<#else>
						<@ww.datepicker label="${field.cname}" name="${field.name}" size="10" cssClass="${cssClass}" 
							format="%Y%m%d" theme="${theme}" /><#break>
					</#if>
					<#break>
				<#default>
					<@ww.textfield label="${field.cname}" name="${field.name}" cssClass="${cssClass}" 
						readonly="${readonly}" theme="${theme}"/><#break>
			</#switch>
	</#switch>
</#if>