<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base target="mainFrame">
<link href="${request.contextPath}/css/menu.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="${request.contextPath}/js/menu2.js"></script>
<SCRIPT language="JavaScript" src="${request.contextPath}/js/scriptlib.js"></SCRIPT>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<#import "/views/macros/Authorization.flt" as auth> 
</head>
<script language="JavaScript">
Menu["defaultImage_2"] = "images/a.gif";
Menu["expandImage_2"] = "images/a1.gif";
</script>
<body onload="initMenu()" class="body">

<div id="nav"><ul class="level1">

<@auth.menugroup name="客户群管理">
	<@auth.menu permit="/customergroup/query" name="客户群查询"/>
	<@auth.menu permit="/customergroup/create" name="新增客户群"/>
	<@auth.menu permit="/customergroup/eval" name="客户群评价"/>
</@auth.menugroup>
<#--
<@auth.menugroup name="">
	<@auth.menu permit="" name=""/>
	<@auth.menu permit="" name=""/>
	<@auth.menu permit="" name=""/>
	<@auth.menu permit="" name=""/>
	<@auth.menu permit="" name=""/>
</@auth.menugroup>
-->
<@auth.menugroup name="营销计划管理">
	<@auth.menu permit="/promotionplan/query" name="营销计划查询"/>
	<@auth.menu permit="/promotionplan/create" name="新增营销计划"/>
	<@auth.menu permit="/promotionplan/eval" name="营销计划评价"/>
</@auth.menugroup>

<@auth.menugroup name="营销评价">
	<@auth.menu permit="/metric/query" name="评价指标管理"/>
	<@auth.menu permit="/metric/create" name="新增评价指标"/>
	<@auth.menu permit="/evaluation/query" name="评价指标体系管理"/>
	<@auth.menu permit="/evaluation/create" name="新增评价指标体系"/>
</@auth.menugroup>

<@auth.menugroup name="规则管理">
	<@auth.menu permit="/rule/query" name="规则查询"/>
	<@auth.menu permit="/rule/create" name="新增规则"/>
</@auth.menugroup>

<@auth.menugroup name="礼品管理">
	<@auth.menu permit="/gift/gift/query" name="礼品清单"/>
	<@auth.menu permit="/gift/gift/create" name="新增礼品"/>
	<@auth.menu permit="/gift/dispatchinggift/query" name="待配送礼品清单"/>
</@auth.menugroup>

<@auth.auth permit="/report/reportDefinition/query">
	<li class="submenu"><a href='<@ww.url namespace="/report/reportDefinition" action="query" ><@ww.param name="buttons">queryresult</@ww.param></@ww.url>' target="mainFrame">报表查询</a>
	</li>
</@auth.auth>

<@auth.menugroup name="报表管理">
	<@auth.menu permit="/report/reportDefinition/query" name="报表定义管理"/>
	<@auth.menu permit="/report/reportChartDefinition/query" name="图表定义管理"/>
	<@auth.menu permit="/report/reportParameter/query" name="报表参数管理"/>
</@auth.menugroup>

<@auth.menugroup name="任务管理">
	<@auth.menu permit="/task/definition/getTasks" name="任务管理"/>
	<@auth.menu permit="/task/status/getExecutingTasks" name="任务调度"/>
</@auth.menugroup>

<@auth.menugroup name="系统管理">
	<@auth.menu permit="/pointtype/query" name="积分类型管理"/>
	<@auth.menu permit="/user/query" name="用户管理"/>
	<@auth.auth permit="/report/reportDefinition/query">
	</@auth.auth>
	<@auth.auth permit="/role/query">
		<li><a href="${request.contextPath}/todo.html" target="mainFrame">角色管理</a></li>
	</@auth.auth>
	<@auth.auth permit="/user/changepassword">
		<li><a href="${request.contextPath}/todo.html" target="mainFrame">修改密码</a></li>
	</@auth.auth>
</@auth.menugroup>

    
<li><a href='#' onclick='if(window.confirm("是否退出系统？")) {window.top.location.href="${request.contextPath}/system/login!logout.action";} return false;' target="_top" >退出系统</a></li>


</ul></div>
</body>
</html>

 



  
