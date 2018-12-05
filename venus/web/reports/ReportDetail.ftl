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
设置报表参数: ${report.name} 
<!-- EndTemplate -->

<!-- BeginTemplate name="message" --><!-- EndTemplate -->

<!-- BeginTemplate name="content" -->

<@ww.form method="post" >
<table class="inputcontent" border="0" cellpadding="0" cellspacing="0" width="100%">
<tbody>
<#list reportParameters as reportParameterMap>
    <tr>
      <td class="tdLabel" id="${reportParameterMap.reportParameter.description}" >      	
      	  ${reportParameterMap.reportParameter.description}
      	  <#if (reportParameterMap.isRequired()) >
      	    *
      	  </#if>
      </td>     
      <#if (reportParameterMap.reportParameter.type == "Text")>
      <td id="${reportParameterMap.reportParameter.name}">
        <input type="text" name="${reportParameterMap.reportParameter.name}" length="50">       
      </td>
      </#if>
      <#if (reportParameterMap.reportParameter.type == "Date")>
      <td id="${reportParameterMap.reportParameter.name}">
        <@ww.datepicker name="${reportParameterMap.reportParameter.name}" value="${defaultDate}" size="10" format="%Y%m%d" theme="simple" />
      </td>
      </#if>
      <#if (reportParameterMap.reportParameter.type == "Boolean" )>
      <td id="${reportParameterMap.reportParameter.name}">       
        <select id="${reportParameterMap.reportParameter.name}" name="${reportParameterMap.reportParameter.name}">
          <#list reportParameterMap.reportParameter.values as value>
            <option value="${value.id}">${value.description}</option>
          </#list>
        </select>
      </td>
      </#if>
      <#if (reportParameterMap.reportParameter.type == "Query" || reportParameterMap.reportParameter.type == "List")>
      <td id="${reportParameterMap.reportParameter.name}">
       <#if reportParameterMap.reportParameter.isMultipleSelect()>
        <select id="${reportParameterMap.reportParameter.name}" name="${reportParameterMap.reportParameter.name}" size="4" multiple>
       <#else>
        <select id="${reportParameterMap.reportParameter.name}" name="${reportParameterMap.reportParameter.name}">
       </#if>
		  <#if (!reportParameterMap.isRequired()) >
		    <option value="" SELECTED>(None)</option>
		  </#if>
          <#list reportParameterMap.reportParameter.values as value>
            <option value="${value.id}">${value.description}</option>
          </#list>
        </select>
      </td>
      </#if>
    </tr>      
</#list>
</tbody>
</table>
<div class="buttons">
	<input type="submit" value="确定" name="submitType">        
	<input type="hidden" name="reportId" value="${reportId}">        
	<input type="hidden" name="step" value="${step}">      
	<input type="hidden" name="displayInline" value="${displayInline?string}">
	<input value="返回" type="submit" onclick='window.location="<@ww.url namespace="/report/reportDefinition" action="query" ><@ww.param name="buttons">queryresult</@ww.param></@ww.url>";return false;' />
</div>  
</@ww.form>


<!-- EndTemplate -->
</body>
</html>

