<html>
<head>
<!-- BeginTemplate name="title" -->
<title>欢迎登录UC银行客户积分营销系统</title>
<!-- EndTemplate -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<@ww.head />
<!-- BeginTemplate name="script" -->
<link href="${request.contextPath}/css/main.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/reports/dojo.js"></script>   
<script type="text/javascript" src="/reports/highlight.js"></script> 
<script language="JavaScript" type="text/JavaScript">

function setDefaultExportType()
{
	if (optionsForm.exportType.length)
   	{
 		optionsForm.exportType[0].checked=true
 	}
 	else
 	{
 	    optionsForm.exportType.checked=true
 	}
}

function setBlankTarget()
{	
	optionsForm.target="_blank";		
}

function setNoTarget()
{
	optionsForm.target="";		
}
</script>
<!-- EndTemplate -->
</head>
<body>
<!-- BeginTemplate name="head" -->
选择导出选项: ${report.name}
<!-- EndTemplate -->

<!-- BeginTemplate name="message" --><!-- EndTemplate -->

<!-- BeginTemplate name="content" -->

<div align="center">
<form action="reportOptions.action" name="optionsForm" >    
<table class="inputcontent" border="0" cellpadding="0" cellspacing="0" width="100%">
<tbody>
    <tr>
      <td class="tdLabel">导出类型：</td>  
      <td> 
      <#if (report.pdfExportEnabled) >
       <input type="radio" name="exportType" value="0">PDF
      </#if>
      <#if (report.htmlExportEnabled) >
        <input type="radio" name="exportType" value="2">HTML
      </#if>
      <#if (report.csvExportEnabled) >
        <input type="radio" name="exportType" value="3">CSV      
      </#if>
      <#if (report.xlsExportEnabled || report.isJXLSReport()) >
        <input type="radio" name="exportType" value="1">XLS
      </#if> 
      <#if (report.rtfExportEnabled) >
        <input type="radio" name="exportType" value="5">RTF
      </#if> 
       <#if (report.textExportEnabled) >
        <input type="radio" name="exportType" value="6">Text
      </#if> 
       <#if (report.excelExportEnabled) >
        <input type="radio" name="exportType" value="7">Excel
      </#if> 
      <#if (report.jasperReport) >
        <input type="radio" name="exportType" value="4">Image
      </#if>
      <script language="JavaScript" type="text/JavaScript">
        setDefaultExportType()
	  </script> 
		</td>   
    </tr>
</tbody>
</table>
  
<div class="buttons">
	<input type="submit" onClick="setNoTarget()" name="submitRun" value="Run">
	<input type="hidden" name="reportId" value="${reportId}"> 
	<input value="返回" type="submit" onclick='window.location="<@ww.url namespace="/report/reportDefinition" action="query" ><@ww.param name="buttons">queryresult</@ww.param></@ww.url>";return false;' />
</div>  
</form>  
<!-- EndTemplate -->
</body>
</html>
