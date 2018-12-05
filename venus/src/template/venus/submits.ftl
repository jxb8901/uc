<#--
将当前请求交易所在包中的所有交易生成相应的submit标签
生成规则：
	在include中明确指定的交易要生成;
	在exclude中明确指定的交易不生成;
	当前交易默认不生成
	如果指定了include参数，则不生成;
	如果指定了exclude参数，则生成;
	其它情况都生成
标签参数说明：
 @param include 指定要生成submit标签的交易名
 @param exclude 指定不生成submit标签的交易名
-->
<#function acceptableName x>
	<#if parameters.include?exists && parameters.include?default("")?contains(x) ><#return true /></#if>
	<#if parameters.exclude?exists && parameters.exclude?default("")?contains(x) ><#return false /></#if>
	<#if x == "${config.owner.name}"><#return false></#if>
	<#if parameters.include?exists><#return false></#if>
	<#if parameters.exclude?exists><#return true></#if>
	<#return true >
</#function>
<#assign templateSource>
	<#list config.owner.package.transactions as trans>
		<#if acceptableName(trans.name) >
			<${'@'}ww.submit name="action:${trans.name}" value="${trans.cname}" />
		</#if>
	</#list>
</#assign><#rt/>
<#assign inlineTemplate = templateSource?interpret /><#rt/>
<@inlineTemplate />