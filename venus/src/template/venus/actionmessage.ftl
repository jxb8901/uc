<#if (actionMessages?exists && actionMessages?size > 0)>
<div class="message" >
	<ul>
		<#list actionMessages as message>
			<li><span class="actionMessage">${message}</span></li>
		</#list>
	</ul>
</div>
</#if>
