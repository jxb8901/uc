<#assign dummy=parameters.put("label", field.cname)?default("xx") ><#-- 因为simple中的组件不会生成label所以使用此方法让controlheader生成 -->
<#include "/${parameters.templateDir}/xhtml/controlheader.ftl" />

<#if ! field.multi > <#-- 单选 -->
	<@ww.select label="${field.cname}" name=name theme="simple" cssClass="${cssClass}"
			headerKey="" headerValue="请选择${field.cname}" list=list value=value listKey="${listKey}" listValue="${listValue}" />
<#elseif parameters.readonly?default('false') == 'true' > <#-- 多选 只读 -->
	<table border="0"><tr><td>
	<@ww.select label="${field.cname}" name=name cssClass="readonly" size="10" theme="simple" 
		list=firstList value='' listKey="${listKey}" listValue="${listValue}" />
	</td></tr></table>
<#else> <#-- 多选 非只读 -->
	<@ww.optiontransferselect label="${field.cname}" name=name size="10" theme="simple" 
		addAllToLeftLabel="<<" addAllToRightLabel=">>" addToLeftLabel="<" addToRightLabel=">" allowSelectAll="false"
		list=firstList listKey="${listKey}" listValue="${listValue}" doubleName="ingore:${name}"
		doubleList=list doubleListKey="${listKey}" doubleListValue="${listValue}" />
</#if>
<#if parameters.readonly?default('false') == 'false' ><#-- 非只读 -->
	<#if "${field.type}" != "enumt" > <#-- 生成“更多查询...”按钮 -->
		<#assign fieldNameList=action.join(action.ognl('#this.{name}', field.fields), ",") />
		<#assign url><@ww.url action="query" namespace="${field.model.owner.namespace}">
				<@ww.param name="select">1</@ww.param>
				<@ww.param name="type">${field.name}</@ww.param>
			</@ww.url>
		</#assign>
		<#assign button><input type='button' value='...' onclick=showDialog('${request.contextPath}/dialog.jsp?url=${url?trim}') /></#assign>
		<#if ! field.multi>${button}</#if>
		<script language="javascript">
			function onSelect${field.name}(selectedValue) {
				doSelectValue(selectedValue, "${name}", "${field.model.key.name}", "${fieldNameList}");
			}
			<#if field.multi>$("select[@name='${name}']/../../td:eq(1)").append("${button}<br/><br/>");</#if>
		</script>
	</#if>
	<#if field.multi><#-- 覆盖WW脚本中的同名函数，当要增加的元素在to中存在时，不增加该元素 -->
		<#if !stack.findValue("#moveSelectedOptions_generated")?exists><#t/>
			<#assign temporaryVariable = stack.setValue("#moveSelectedOptions_generated", "true") /><#t/>
			<script language="javascript">
				function moveSelectedOptions(from,to) {
					if (!hasOptions(from)) { return; }
					to.selectedIndex = -1;
					for (var i=0; i<from.options.length; i++) {
						var o = from.options[i];
						if (o.selected) {
							var index = -1;
							for (var j = 0; j < to.options.length; j++) {
								if (to.options[j].value == o.value) {
									to.options[j].selected = true;
									index = j;
									break;
								}
							}
							if (index == -1)	to.options[to.options.length] = new Option( o.text, o.value, false, true);
						}
					}
					removeSelectedOptions(from);
				}
			</script>
		</#if>
	</#if>
</#if>

<#include "/${parameters.templateDir}/xhtml/controlfooter.ftl" />
