<#if  request.getAttribute("formtype")?default("input") == "input" >
</table>${request.setAttribute("tableclosed", "closed")}
</#if>
<#include "/${parameters.templateDir}/simple/div.ftl" />