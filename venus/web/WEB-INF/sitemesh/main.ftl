<html><#-- 注意：模板文件中必须调用response.setContent，且contentType必须为text/html -->
${response.setContentType("text/html; charset=UTF-8")}<#rt/>
${response.setHeader("Pragma", "no-cache")}<#rt/>
${response.setHeader("Cache-Control", "no-cache")}<#rt/>
${response.setDateHeader("Expires", 0)}<#rt/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- BeginTemplate name="title" --><title>欢迎登录积分营销系统</title><!-- EndTemplate -->
<link rel="stylesheet" href="${request.contextPath}/css/main.css" type="text/css" />
<script type="text/javascript" src="${request.contextPath}/js/scriptlib.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/jquery-latest.js"></script>
<@ww.head />
<!-- BeginTemplate name="script" --><!-- EndTemplate -->
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
	<td height="100%" valign="top" class="container1">
		<div class="heading">
			<!-- BeginTemplate name="head" -->
				${(config.owner.package.cname)?default("")} - ${(config.cname)?default("")}
			<!-- EndTemplate -->
		</div>
		<!-- BeginTemplate name="error" --><@ww.actionerror /><!-- EndTemplate -->
		<!-- BeginTemplate name="message" --><@ww.actionmessage /><!-- EndTemplate -->
		<!-- BeginTemplate name="content" --><!-- EndTemplate -->
	</td>
</tr>
<tr>
	<td valign="bottom" class="container2">
		<div class="remark"><div class="remark_title">备注：</div>
            <ul>
              <li></li>
            </ul>
		</div>
	</td>
</tr>
<tr>
	<td height="50" align="center" style="font-size:10px">&copy; All RIGHTS RESERVED. 版权所有 2007　 地址:香港路43号　电话:123456</td>
</tr>
</table>
</body>
<script language='javascript'>
hiddenRemarkIfEmpty();
forcusOnFormInput();
</script>
</html>
