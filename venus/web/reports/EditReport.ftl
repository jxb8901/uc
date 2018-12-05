<html>
<!-- BeginTemplate name="title" -->
<title>欢迎登录UC银行客户积分营销系统</title>
<!-- EndTemplate -->
<!-- BeginTemplate name="script" -->

<style type="text/css">

	body {
		font-family : sans-serif;
	}
	
	.dojoTabPaneWrapper {
  	padding : 10px 10px 10px 10px;
	}

</style>
<script type="text/javascript">
</script>
<!-- EndTemplate -->
<!-- BeginTemplate name="head" -->
				报表定义管理 - 报表参数管理 : ${(report.name)!""}
<!-- EndTemplate -->

<!-- BeginTemplate name="message" --><!-- EndTemplate -->
<!-- BeginTemplate name="content" -->

<table class="listcontent" width="100%" border="0" cellpadding="0" cellspacing="0"> 
<thead>
    <tr>
      <td colspan="2">Report Parameter</td>
      <td>Required</td>     
      <td>Step</td>
      <td>Sort Order</td>
      <td colspan="2">&nbsp;</td>
    </tr>   
 </thead>
 <tbody>
    <#list parametersInReport as parameterInReport >
    <form action="editReportParameterMap.action"  >
    <tr class="a">   	 
   	  <td colspan="2">
   	    ${parameterInReport.reportParameter.name}
   	  </td>       
   	  <td>
   	    <input type="checkbox" name="required" value="true" <#if parameterInReport.required> CHECKED </#if>>   	   
   	  </td>       	      	  
   	  <td>
   	    <input type="text" size="2" name="step" value="${parameterInReport.step}">   	    
   	  </td>  
   	  <td>
   	    <input type="text" size="2" name="sortOrder" value="${parameterInReport.sortOrder}">   	    
   	  </td>  
   	  <td class="dialogButtons">
   	  	<input class="standardButton" type="submit" name="submitUpdate" value="Update">
   	  </td> 
   	  <td class="dialogButtons">
   	  	<input class="standardButton" type="submit" name="submitDelete" value="Delete">
   	  </td> 
    </tr>   
    <input type="hidden" name="id" value="${id}">
    <input type="hidden" name="command" value="${command}"> 
    <input type="hidden" name="reportParameterId" value="${parameterInReport.reportParameter.id}"/>    
    </form>
   </#list>     
    <form action="editReportParameterMap.action"  >     
    <tr class="a">
      <td colspan="7"><hr></td>
    </tr>
    <tr>
      <td colspan="2">
        <select name="reportParameterId">           
          <#list reportParameters as reportParameter>
            <option value="${reportParameter.id}">${reportParameter.name}
	   </#list>
        </select>
      </td>       
      <td class="dialogButtons" >        
      	<input class="standardButton" type="submit" name="submitAdd" value="Add">
      </td>             
    </tr>
    <input type="hidden" name="id" value="${id}"> 
  	<input type="hidden" name="command" value="${command}"> 
  	</form>    
 </tbody>
  </table>
<!-- EndTemplate -->

