<#--
	Only show message if errors are available.
	This will be done if ActionSupport is used.
-->
<#assign hasFieldErrors = parameters.name?exists && fieldErrors?exists && fieldErrors[parameters.name]?exists/>
<#if hasFieldErrors>
<span class="errorMessage">
<#list fieldErrors[parameters.name] as error>
	${error?html};<#t/>
</#list>
</span>
</#if>