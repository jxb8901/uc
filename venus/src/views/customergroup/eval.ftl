<html>
<head>
<!-- BeginTemplate name="title" -->
<title>欢迎登录UC银行客户积分营销系统</title>
<!-- EndTemplate -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- BeginTemplate name="script" -->
<style type="text/css">
<!--
table.listcontent td.leftheader {
	width:12%;
	background-color:#5795c2;
	font-size:12px;
	text-align:center;
	color:#fff;
}
-->
</style>
<script>
function showChart(frequence, metric, dimensionType, dimension) {
	document.getElementById('chart_form').frequence.value = frequence;
	document.getElementById('chart_form').metric.value = metric;
	document.getElementById('chart_form').dimensionType.value = dimensionType || '*';
	document.getElementById('chart_form').dimension.value = dimension || '*';
	document.getElementById('chart_form').submit();
}
</script>
<!-- EndTemplate -->
</head>
<body>
<!-- BeginTemplate name="head" --><!-- EndTemplate -->

<!-- BeginTemplate name="message" --><!-- EndTemplate -->

<!-- BeginTemplate name="content" -->
<@ww.form method="post" >
	<@ww.component template="forminput.ftl" />
	<@ww.div cssClass="buttons">
		<@ww.submit value="确定" tooltip="确定" />
	</@ww.div>

<#list result as values>
<#switch "${values.frequence}">
	<#case "D"><#assign jsformat="%Y%m%d" /><#assign dateformat="yyyyMMdd" /><#break/>
	<#case "M"><#assign jsformat="%Y%m%d" /><#assign dateformat="yyyyMM" /><#break/>
	<#case "Q"><#assign jsformat="%Y%m%d" /><#assign dateformat="yyyyMM" /><#break/>
	<#case "Y"><#assign jsformat="%Y%m%d" /><#assign dateformat="yyyy" /><#break/>
	<#default><#assign jsformat="%Y%m%d" /><#assign dateformat="yyyyMMdd" /><#break/>
</#switch> 
<table class="listcontent" width="100%" border="0" cellpadding="0" cellspacing="0">
	<thead>
		<tr>
			<#assign colspan=values.metrics?size + 1 />
			<#list values.metrics as metricinfo><#assign colspan=colspan+metricinfo.dimensions?size /></#list>
			<td colspan="${colspan}">
				频度：${values.frequence}&nbsp;
				范围：
				<@ww.datepicker name="daterange_${values.frequence}_Start" value="${values.dateRange.start.toDate()?string('yyyyMMdd')}" size="10" format="${jsformat}" theme="simple" />
				到
				<@ww.datepicker name="daterange_${values.frequence}_End" value="${values.dateRange.end.toDate()?string('yyyyMMdd')}" size="10" format="${jsformat}" theme="simple" />		
			</td>
		</tr>
		<tr>
			<td rowspan="3">&nbsp;</td>
			<#list values.metrics as metricinfo>
			<td colspan="${metricinfo.dimensions?size+1}">${metricinfo.metric.name}</td>
			</#list>
		</tr>
		<tr>
			<#list values.metrics as metricinfo>
				<td rowspan="2"><a href='#' onclick='return showChart("${values.frequence}", "${metricinfo.metric.name}")' title="查看图形">总额</a></td>
				<#list metricinfo.dimensionTypes as dt>
				<td colspan="${metricinfo.getDimensions(dt)?size}">${dt}</td>
				</#list>
			</#list>
		</tr>
		<tr>
			<#list values.metrics as metricinfo>
				<#list metricinfo.dimensionTypes as dt>
					<#list metricinfo.getDimensions(dt) as dim>
					<td><a href='#' onclick='return showChart("${values.frequence}", "${metricinfo.metric.name}", "${dt}", "${dim}")' title="查看图形">${dim}</a></td>
					</#list>
				</#list>
			</#list>
		</tr>
	</thead>
	<tbody>
		<#list values.dateRange.iterator() as frequenceDate>
		<tr>
			<td class="leftheader">${frequenceDate.toDate()?string(dateformat)}</td>
			<#list values.getValues(frequenceDate.toDate()) as value >
			<td>${value?default('xx')}</td>
			</#list>
		</tr>
		</#list>
	</tbody>
</table>
</#list>
</@ww.form>
<@ww.form namespace="/reports" action="evaluationChartReport" method="post" type="list" id="chart_form">
	<input type="hidden" name="frequence"/>
	<input type="hidden" name="metric"/>
	<input type="hidden" name="dimensionType"/>
	<input type="hidden" name="dimension"/>
</@ww.form>
<!-- EndTemplate -->
</body>
</html>
