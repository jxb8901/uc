<html>
<head>
<!-- BeginTemplate name="title" -->
<title>欢迎登录UC银行客户积分营销系统</title>
<!-- EndTemplate -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<@ww.head />
<!-- BeginTemplate name="script" -->

<!-- EndTemplate -->
</head>
<body>
<!-- BeginTemplate name="head" -->
<#if !(report?exists && report.displayInline) >
${report.name} 
</#if>
<!-- EndTemplate -->

<!-- BeginTemplate name="message" --><!-- EndTemplate -->

<!-- BeginTemplate name="content" -->

<div align="center">

<#assign ds=JspTaglibs["http://displaytag.sf.net"] />
<#assign tmp=request.setAttribute("results", results) />

<@ds.table name="results" class="listcontent" sort="list" export=false pagesize=10 >   
 	<#list properties as property >		
 		<@ds.column property="${property.name}" title="${property.displayName}" sortable=true headerClass="sortable" decorator="${property.decorator?default('')}" /> 	     
	</#list>
	<@ds.setProperty name="basic.msg.empty_list" value="没有记录" />
	<@ds.setProperty name="paging.banner.group_size" value="8" />
	<@ds.setProperty name="paging.banner.placement" value="bottom" />
	<@ds.setProperty name="export.pdf" value="false" />
	<@ds.setProperty name="export.pdf.filename" value="${report.name}.pdf" />  
	
	<@ds.setProperty name="paging.banner.all_items_found" >
	</@ds.setProperty>
	<@ds.setProperty name="paging.banner.some_items_found" >
	</@ds.setProperty>
	<@ds.setProperty name="paging.banner.full" >
		<div class="pager" align="right">
		共${results?size}条{5}/{6}页[<a href="{1}">首页</a>][<a href="{2}">上一页</a>]
		[<a href="{3}">下一页</a>][<a href="{4}">尾页</a>]
		</div>
	</@ds.setProperty>
	<@ds.setProperty name="paging.banner.first" >
		<div class="pager" align="right">
		共${results?size}条{5}/{6}页[首页][上一页]
		[<a href="{3}">下一页</a>][<a href="{4}">尾页</a>]
		</div>
	</@ds.setProperty>
	<@ds.setProperty name="paging.banner.last" >
		<div class="pager" align="right">
		共${results?size}条{5}/{6}页[<a href="{1}">首页</a>][<a href="{2}">上一页</a>]
		[下一页][尾页]
		</div>
	</@ds.setProperty>
</@ds.table>

<#if !(report?exists && report.displayInline) >
<form action='<@ww.url namespace="/report/reportDefinition" action="query" ><@ww.param name="buttons">queryresult</@ww.param></@ww.url>' method="post" >
<div class="buttons"><input value="返回" type="submit"></div>
</form>
</#if>
</div>
<!-- EndTemplate -->
</body>
</html>