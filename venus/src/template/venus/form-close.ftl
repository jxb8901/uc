<#if  request.getAttribute("formtype")?default("input") == "input" &&
		request.getAttribute("tableclosed")?default("noclose") == "noclosed" >
	<#include "/${parameters.templateDir}/venus/control-close.ftl" />
</#if>
<#include "/${parameters.templateDir}/simple/form-close.ftl" />
<#include "/${parameters.templateDir}/xhtml/form-close-validate.ftl" />