<html>
<head>
<title>欢迎登录UC银行客户积分营销系统！</title>
<@ww.head theme="venus"/>
<link rel="stylesheet" href="${request.contextPath}/css/main.css" type="text/css">
<script language="JavaScript" src="${request.contextPath}/js/scriptlib.js"></script>
<style type="text/css">
<!--
.error {
	background-color:transparent;
}
#main {
	padding:100px 0 0 240px;
	width:600px;
	height:420px;
	background-image:url(../images/personloginbg.gif); 
	background-repeat:no-repeat; 
}
.title {
	width:310px;
	height:10px;
	text-align:center; 
	font-size:16px; 
	font-weight:bolder;
}
.wwFormTable {
	width:310px;
	height:128px;
	font-size:12px
}
.wwFormTable td {
	width: 70%;
	vertical-align:middle;
}
.wwFormTable td.tdLabel {
	width: 30%;
	text-align:right;
}
.button {
	border:1px solid #cccccc;
	background-color:#fff;
	background-image:none;
	color:#FF3300;
}
-->
</style>

</head>
<body>

<div id="main">
<p class="title">欢迎登录客户积分营销系统</p>
<@ww.actionerror />
<@ww.form method="post" validate="false" theme="xhtml" namespace="/system" action="login!submit">
    <@ww.textfield label="用户名" name="userid"/>
    <@ww.password label="密码" name="password"/>
    <@ww.submit value="登录" align="center" cssClass="button"/>
</@ww.form>
</div>
</body>
<script language='javascript'>
hiddenRemarkIfEmpty();
forcusOnFormInput();
</script>
</html>

