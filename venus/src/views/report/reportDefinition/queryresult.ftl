
<input value="查询报表" type="submit" onclick="return queryresult(this.form);">
<script>
function queryresult(form) {
	var idno = getCheckedRadioValue("idno");
	if (idno) {
		window.location = "${request.contextPath}/reports/reportDetail.action?reportId=" + idno;
		return false;
	}
	else {
		alert("请先选择报表！");
		return false;
	}
}
</script>
