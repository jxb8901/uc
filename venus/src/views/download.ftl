${
response.setContentType("application/x-msdownload; charset=gb2312")
}<#-- ${
response.setContentLength((int)fileLen)
} -->


<%@ page contentType="application/x-msdownload; charset=gb2312" errorPage="/gb/errorpage.jsp"%><%@ 
taglib uri="/WEB-INF/c.tld" prefix="c" %><%@ 
taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %><%@ 
taglib uri="/WEB-INF/tlds/ebank.tld" prefix="ebank" %><%!
String[][] templates = new String[][]{
		{"salary.xls", "代发工资文件模板.xls"},
		{"transfer.xls", "批量转帐文件模板.xls"}
};
%><%
	//response.setContentType("application/x-msdownload; charset=gb2312");
	//response.setContentLength((int)fileLen);
	int type = 0;
	try {
		type = Integer.parseInt(request.getParameter("type"));
	}catch(Exception e){}
	
	response.setHeader("Content-Disposition", "attachment; filename="+
		new String(templates[type][1].getBytes(), "iso8859-1"));
		
	javax.servlet.ServletOutputStream output = response.getOutputStream();
	File file = new File(request.getRealPath("/"+"/gb/templates/"+templates[type][0]));
	
	response.setContentLength((int)file.length());
	FileInputStream in = new FileInputStream(file);
	byte[] buf = new byte[1000];
	int readCount;
	while ((readCount = in.read(buf,0,1000)) != -1) {
	output.write(buf, 0, readCount);
	}
%>
