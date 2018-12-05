<!--
<@ww.submit type="button" namespace="/reports" name="action:editReport" label="编辑报表参数" />
-->

<input value="编辑报表参数" type="submit" onclick="return editReportParas(this.form)">
<input type="hidden" name="command" value="edit">
<script type="text/javascript" >
function editReportParas(form){
	//alert(form.action);
	if(!getCheckedRadioValue("idno")){
		alert("请先选择要编辑的报表!");
		return false;
	}
	form.action="${request.contextPath}/reports/editReport.action";
	return true;
}
</script>
