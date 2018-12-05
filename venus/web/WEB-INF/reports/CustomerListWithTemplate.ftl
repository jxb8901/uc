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
<script language="JavaScript" src="date-picker.js"></script>
<!-- EndTemplate -->
</head>
<body>
<!-- BeginTemplate name="head" -->
<strong>Customer List: </strong>            		
Click on the 'View' links to view DrillDown reports.
<!-- EndTemplate -->

<!-- BeginTemplate name="message" --><!-- EndTemplate -->

<!-- BeginTemplate name="content" -->
<#include "/reports/Banner.ftl" />
<#include "/reports/Errors.ftl" />

<div align="center">

	<a class="back-link img-report-small" href="reportList.action">Back to Reports</a>	  
	<a class="back-link img-group-small" href="reportGroup.action">Back to Groups</a> 		  
  		
  	<br/><br/>
<#assign ds=JspTaglibs["http://displaytag.sf.net"] />
<#assign tmp=request.setAttribute("results", results) />

<@ds.table name="results" class="displayTag" sort="list" export=true pagesize=10 >  		
	<@ds.column property="customername" title="Name" sortable=true headerClass="sortable" />	     
	<@ds.column property="city" title="City" sortable=true headerClass="sortable" />	     
	<@ds.column property="country" title="Country" sortable=true headerClass="sortable" />
	<@ds.column value="View Report" title="Order List" href="executeReport.action?reportName=Orders By Customer" paramId="CustomerNumber" paramProperty="customernumber" />
	<@ds.column value="View Chart" title="Order Break Down"   href="executeReport.action?reportName=Orders By Product Line" paramId="CustomerNumber" paramProperty="customernumber" />  	     		
	<@ds.setProperty name="export.pdf" value="true" />
	<@ds.setProperty name="export.xml" value="false" />
	<@ds.setProperty name="export.pdf.filename" value="${report.name}" />
	<@ds.setProperty name="export.csv.filename" value="${report.name}" />
	<@ds.setProperty name="export.excel.filename" value="${report.name}" />
</@ds.table>


Schedule Report: 
<a href="reportOptions.action?reportId=${report.id}&submitSchedule=true&exportType=3">CSV</a> |
<a href="reportOptions.action?reportId=${report.id}&submitSchedule=true&exportType=1">Excel</a> |
<a href="reportOptions.action?reportId=${report.id}&submitSchedule=true&exportType=0">PDF</a>

<!-- EndTemplate -->
</body>
</html>

