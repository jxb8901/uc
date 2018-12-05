<#assign templateSource>
	<#list config.inputViewFields as field>
		<${'@'}ww.component template="field.ftl" index="${field_index}" />
	</#list>
</#assign><#rt/>
<#assign inlineTemplate = templateSource?interpret /><#rt/>
<@inlineTemplate />