<#if stack.findValue("config.inputViewFields.{tagtype}")?default([])?seq_contains("file") >
	${tag.addParameter("enctype", "multipart/form-data")}
</#if>
${request.setAttribute("formtype", parameters.type?default("input"))}
<#include "/${parameters.templateDir}/xhtml/form-validate.ftl" />
<#include "/${parameters.templateDir}/simple/form.ftl" />
<#if parameters.type?default("input") == "input">
<#include "/${parameters.templateDir}/venus/control.ftl" />
</#if>