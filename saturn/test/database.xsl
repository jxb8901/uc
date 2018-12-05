<?xml version="1.0" encoding="gb2312"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
 
    <xsl:output method="html"/>

    <xsl:template match="/">
        <HTML>
            <head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="style.css" rel="stylesheet" type="text/css" />

<style>
body, div, td, th, p {
  font-family: verdana, sans-serif;
  font-size: 12px;
  font-weight: normal;
}

/*/*/a{}
body,
body div,
body td,
body th,
body p {
  font-size: 12px;
  font-weight: normal;
  voice-family: "\"}\"";
  voice-family: inherit;
  font-size: 12px;
  font-weight: normal;
}
html>body,
html>body div,
html>body td,
html>body th
html>body p {
  font-size: 12px;
  font-weight: normal;
}  
/* */

body {
	background-color:#fff;
	margin: 10px;
	margin-left: 10px;
	margin-right: 10px;
	 scrollbar-face-color: #CAE4FB; 
	 scrollbar-shadow-color: #CAE4FB; 
     scrollbar-highlight-color: #CAE4FB; 
	 scrollbar-3dlight-color: #BBDEFB; 
     scrollbar-darkshadow-color: #BBDEFB; 
	 scrollbar-track-color: #fff; 
	 scrollbar-arrow-color: #336699;
}


#datalist,.table_list,.table_input {
	background-color: #E3FEF1;
	border:#009900 1px solid; 
	border-bottom:none;
	border-left:none;
}
.table_input {
    border-left:none;
	border-top:none;
	border-bottom:#009900 1px solid;
	}
.tabel_list {
	text-align: center;
}

.table_list_head {
	background-color: #CFF0D0;	
	height: 21px;
}

.table_list_head_td {
	font-size: 12px;
	border-bottom:#009900 1px solid ; border-left:#009900 1px solid;
	height:20px;
	padding-top:4px;
	padding-left:3px;
}

.table_list_body_even, .table_list_body_odd, .table_list_body {
	border-bottom:#009900 1px solid;border-left:#009900 1px solid ; font-size: 12px;
	color: #333333;
	font-family: Arial, Batang, "宋体";
	text-align: left;
}

.table_list_body_even {
	background-color: #F7FDF6;
	height:20px;
}

.table_list_body_odd {
	background-color: #fff;
	height:20px;
}

.table_list_body, .table_list_body_loop {
	padding: 0.2em 0.5em 0.2em 0.5em;
	height:20px;
}
.table_list_body_loop {
	background-color: #FCEBB1;
   font-size:12px;
   color:#000;
}

.table_list_head_td a, .table_list_head_td a:link, .table_list_head_td a:visited, 
.table_list_head_td a:hover, .table_list_head_td a:active, 
.table_list_body a, .table_list_body a:link, .table_list_body a:visited, 
.table_list_body a:hover, .table_list_body a:active {
	text-decoration: none; 
	font-size: 12px;
	font-weight: normal;
	color: #0000FF;
}
.table_list_body a:link {
	color: #0000FF
}

h1
	{margin-top:6.0pt;
	margin-right:0cm;
	margin-bottom:3.0pt;
	margin-left:0cm;
	text-indent:0cm;
	page-break-after:avoid;
	layout-grid-mode:char;
	font-size:14px;
	font-family:Arial;
    list-style: none;}
h2
	{margin-top:6.0pt;
	margin-right:0cm;
	margin-bottom:3.9pt;
	margin-left:0cm;
	text-indent:0cm;
	page-break-after:avoid;
	layout-grid-mode:char;
	font-size:14px;
	font-family:Arial;
   list-style: none;
   font-weight: normal;}
h3
	{margin-top:6px;
	margin-right:0cm;
	margin-bottom:6px;
	margin-left:0cm;
	text-indent:0cm;
	page-break-after:avoid;
	layout-grid-mode:char;
	font-size:13px;
	font-family:"Times New Roman";
   list-style: none;
   color:#D24B10;}
</style>

            </head>
            <BODY>
                <xsl:apply-templates select="//table" />
            </BODY>
        </HTML>
    </xsl:template>
    
    <xsl:template match="table">
    	<table class="table_list" border="1" cellpadding="0" cellspacing="0">
    	  <thead class="table_list_head">
    	  	<tr>
    	  		<td class="table_list_head_td" colspan="3">
							<h2><xsl:value-of select="@name"/>　<xsl:value-of select="@alias"/></h2>    	  		
    	  		</td>
    	  	</tr>
            <tr>
              <td class="table_list_head_td">名称</td>
              <td class="table_list_head_td">别名</td>
              <td class="table_list_head_td">是否动态属性</td>
            </tr>
          </thead>
          <tbody>
            <xsl:for-each select="column">
	            <tr class='table_list_body_even'>
	          	  <td class="table_list_body"><xsl:value-of select="@name"/>　</td>
	          	  <td class="table_list_body"><xsl:value-of select="@alias"/>　</td>
	          	  <td class="table_list_body"><xsl:value-of select="@type"/>　</td>
	            </tr>
            </xsl:for-each>
    	  </tbody>
    	</table>
			<br/>
    </xsl:template>

</xsl:stylesheet>
