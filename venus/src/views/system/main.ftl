<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>欢迎登录UC银行</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script  language="JavaScript">
if( self != top ) { top.location.replace(self.location.href); }
</script>
</head>
<body style="margin:0;padding:0">
  <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">

    <tr id="topId">
      <td height="80" width="100%" colspan="3">
        <iframe src="<@ww.url action='top' />" name="topFrame" width="100%" height="80" scrolling="no" frameborder="no"></iframe>
      </td>
    </tr>
    <tr>
      <td colspan="3" align="center" valign="middle" width="100%" height="3" background="${request.contextPath}/images/s2.gif" onclick1="javascript:displayImg('topId');"><!-- a href="#"><img id="topId_splitImg" border="0" width="50" height="8" basename="images/j1" src="images/j1.gif" onmouseover="this.src=this.getAttribute('basename')+'s.gif'" onmouseout="this.src=this.getAttribute('basename')+'.gif'"></a --></td>
    </tr>

    <tr>
      <td width="163" height="100%" align="left" valign="top" id="leftId">
        <iframe src="<@ww.url action='left' />" name="leftFrame" width="163" height="100%" scrolling="no" frameborder="no"></iframe>
      </td>
      <td valign="middle" width="3" height="100%" background="${request.contextPath}/images/s1.gif" onclick1="javascript:displayImg('leftId');">
        <div style="width:3px"></div>
      </td>
      <td width="100%" height="100%" align="left" valign="top" nowrap>

        <iframe src="<@ww.url action='home' />" name="mainFrame" width="100%" height="100%" frameborder="no"></iframe>
      </td>
    </tr>
  </table>
</body>
</html>

