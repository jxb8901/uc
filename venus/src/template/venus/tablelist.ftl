<#--
列表组件，根据结果集生成HTML的table元素

参数：
 @param page 指定分页对象，如果不指定，默认为"page"，指定为"none"表示不分页
 @param list 指定数据源，如果不指定，则首先检查是否存在分页对象page,如果存在
 		则使用分页对象中的record作为数据源，如果不存在，则检查是否存在名为
 		result的数据对象，如果存在则使用之。
-->
<#if parameters.page?exists>
	<#if "none" != parameters.page>
		<#assign pageObject=parameters.page />
	</#if>
<#elseif page?exists>
	<#assign pageObject=page />
</#if>
<#if parameters.list?exists>
	<#assign recordsObject=parameters.list />
<#elseif pageObject?exists>
	<#assign recordsObject=pageObject.records />
<#elseif result?exists && result?is_enumerable >
	<#assign recordsObject=result />
</#if>
<#if recordsObject?exists>
<table class="listcontent" width="100%" border="0" cellpadding="0" cellspacing="0"> 
	<thead> 
		<tr>
			<#list config.outputViewFields as field>
				<td fieldname="${field.name}">${field.cname}</td>
			</#list>
		</tr> 
	</thead> 
	<tbody> 
		<#list recordsObject as record>
			<tr> 
				<#assign dummy=stack.push(record) >
				<#list config.outputViewFields as field>
					<td fieldvalue="<@ww.property value="${field.name}" />">&nbsp;
					<@ww.component template="field.ftl" index="${field_index}" type="output" /></td>
				</#list>
				<#assign dummy=stack.pop() >
			</tr> 
		</#list>
	</tbody> 
</table>
<#else>
<!-- 找不到记录 -->
</#if>
<#-- 分页 -->
<#if pageObject?exists>
<div class="pager" align="right">
      共${pageObject.recordCount}条${pageObject.current}/${pageObject.count}页
    <input type="hidden" id="__currentPage__" name="currentPage" value="${pageObject.current}" />
    <input type="hidden" name="pageSize" value="${pageObject.size}" />
	<#if (pageObject.current > 1) >
	[<a href="1" onclick="doPage('1');return false;">首页</a>]
	</#if><#if (pageObject.current > 1) >
	[<a href="${pageObject.current - 1}" onclick="doPage('${pageObject.current - 1}');return false;">上一页</a>]
	</#if><#if (pageObject.current < pageObject.count) >[<a href="${pageObject.current + 1}" onclick="doPage('${pageObject.current + 1}');return false;">下一页</a>]
	</#if><#if (pageObject.current < pageObject.count) >[<a href="${pageObject.count}" onclick="doPage('${pageObject.count}');return false;">尾页</a>]
	</#if>
	<script>
		function doPage(pageNum) {
			var theForm = document.getElementById("__currentPage__").form;
			theForm.currentPage.value = pageNum;
			<#if parameters.action?exists>
			theForm.action = "${parameters.action}";
			</#if>
			theForm.submit();
		}
	</script>
</div>
</#if>
<#-- 隐藏参数 -->
<@ww.component template="hideparams.ftl" include="@pageSize,currentPage" inputfields="$true" />