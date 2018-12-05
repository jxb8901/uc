<html>
<head>
<!-- BeginTemplate name="title" -->
<title>欢迎登录UC银行客户积分营销系统</title>
<!-- EndTemplate -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<@ww.head />
<!-- BeginTemplate name="script" -->
<link href="${request.contextPath}/css/main.css" rel="stylesheet" type="text/css">
<link href="${request.contextPath}/reports/openreports.css" rel="stylesheet" type="text/css">    
<script type="text/javascript" src="/reports/dojo.js"></script>   
<script type="text/javascript" src="/reports/highlight.js"></script> 
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

<#if !(report?exists && report.displayInline) >
	<#if metric?exists>
	<#assign url><@ww.url namespace="/report/reportDefinition" action="query" ><@ww.param name="buttons">queryresult</@ww.param></@ww.url></#assign>
	<#else>
	<#assign url="javascript:window.back(-1);" />
	</#if>
	<a class="back-link img-group-small" href='${url}'>返回</a> 	
</#if>

<div align="center">

 <br/>

${imageMap}

<img border="0" usemap="#chart" src="imageLoader.action?imageName=ChartImage"/>	
	
<br/>	
<br/>

<#if report.reportChart.showValues >

<#assign ds=JspTaglibs["http://displaytag.sf.net"] />
<#assign tmp=request.setAttribute("chartValues", chartValues) />

<#if (report.reportChart.chartType == 0) >

	<@ds.table name="chartValues" class="listcontent" style="width:75%;" sort="list" > 	   
		<@ds.column property="category" title="${report.reportChart.xaxisLabel?default('Category')}" sortable=true headerClass="sortable" />
		<@ds.column property="value" title="${report.reportChart.yaxisLabel?default('Value')}" sortable=true headerClass="sortable" style="text-align:right;" decorator="net.ninecube.reports.util.NumberColumnDecorator" /> 	     
	</@ds.table>

<#elseif (report.reportChart.chartType == 1 || report.reportChart.chartType == 4) >

	<@ds.table name="chartValues" class="listcontent" style="width:75%;" sort="list" >		<@ds.column property="key" title="${report.reportChart.xaxisLabel?default('Key')}" sortable=true headerClass="sortable" />
		<@ds.column property="value" title="${report.reportChart.yaxisLabel?default('Value')}" sortable=true headerClass="sortable" style="text-align:right;" decorator="net.ninecube.reports.util.NumberColumnDecorator" />
	</@ds.table>

<#elseif (report.reportChart.chartType == 2 ) >

	<@ds.table name="chartValues" class="listcontent" style="width:75%;" sort="list" >
		<@ds.column property="series" title="Series" sortable=true headerClass="sortable" />
		<@ds.column property="value" title="${report.reportChart.xaxisLabel?default('Value')}" sortable=true headerClass="sortable" style="text-align:right;" decorator="net.ninecube.reports.util.NumberColumnDecorator" />     
		<@ds.column property="secondValue" title="${report.reportChart.yaxisLabel?default('Second Value')}" sortable=true headerClass="sortable" style="text-align:right;" decorator="net.ninecube.reports.util.NumberColumnDecorator" />
	</@ds.table>

<#elseif (report.reportChart.chartType == 3 ) >

	<@ds.table name="chartValues" class="listcontent" style="width:75%;" sort="list"  >
		<@ds.column property="series" title="Series" sortable=true headerClass="sortable"  />
		<@ds.column property="time" title="${report.reportChart.xaxisLabel?default('Time')}" sortable=true headerClass="sortable" decorator="net.ninecube.reports.util.DateColumnDecorator" />
		<@ds.column property="value" title="${report.reportChart.yaxisLabel?default('Value')}" sortable=true headerClass="sortable" style="text-align:right;" decorator="net.ninecube.reports.util.NumberColumnDecorator" /> 	     
	</@ds.table>

</#if>

</#if>

</div>
<!-- EndTemplate -->
</body>
</html>

