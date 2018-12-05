<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<body bgcolor="#FFFFFF" text="#000000">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%">
  <tr> 
    <td height="100%" width="100%">
      <iframe src='<%=request.getQueryString().substring("url=".length())%>'  id="winDetail" width="100%" height="100%"></iframe>
    </td>
  </tr>
</table>
</body>
</html>

