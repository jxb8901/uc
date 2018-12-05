
//
function showDialog(url, height, width){
	if (!height) height = 550;
	if (!width) width = 800;
	if (window.showModalDialog) {
		window.showModalDialog(url,window,"dialogHeight:"+height+"px;dialogWidth:"+width+"px;center:1;status:0;scroll:0;help:0;resizable:1;");
	}
	else if (window.open) {
		window.open(url,"new","chrome,dialog,modal,height="+height+",width="+width+"");
	}
	else {
		alert('操作不能执行,因为浏览器不支持弹出对话框!');
	}
}

function doSelect(type) {
	var index = -1;
	$("table.listcontent/tbody//:radio").each(
		function(i, item) {
			if (item.checked) index = i;
		});
	if (index == -1) {
		alert("no selected!");
		return false;
	}
	
	var selectedValue = {};
	var x = $("table.listcontent/thead//td");
	$("table.listcontent/tbody//tr:eq("+index+")/td").each(
		function(i, item) {
			selectedValue[$(x.get(i)).attr("fieldname")] = $(item).attr("fieldvalue");
		});

	if (window.dialogArguments && window.dialogArguments["onSelect"+type]) {
		window.dialogArguments["onSelect"+type](selectedValue);
	}
	else if (window.parent.opener && window.parent.opener["onSelect"+type]) {
		window.parent.opener["onSelect"+type](selectedValue);
	}
	
	if (window.parent.close) window.parent.close();
}

function doSelectValue(selectedValue, selectname, keyName, nameList) {
	var selectpath = "select[@name="+selectname+"]";
	
	var v=selectedValue[keyName];
	var hasValue=false;
	$(selectpath+" option").each(function(i, item) {
		if (v == item.value) {
			hasValue=true;
			item.selected=true;
			return false;
		}
	});
	
	if (!hasValue) {
		var text="";
		$.each(nameList.split(/[, ]+/), function(i, item) {
			if (selectedValue[item]) text = text + " " + selectedValue[item];
		});
		$(selectpath).append("<option selected='selected' value='"+v+"'>"+text+"</option>");
	}
}

/**
 * 禁用页面所有可视的输入控件，不包括按钮
 * @param f 页面表单元素(form)
 */
function disableAllElements(f) {
    for(var i = 0; i < f.length; i++) {
        var e = f.elements[i];
        if (e.type == "text" || e.type == "textarea" || e.type == "checkbox" || e.type == "radio" || e.tagName == "SELECT") {
            e.disabled = true;
        }
    }
}

/**
 * 取已选中的元素，返回结果总是一个数组
 */
function getCheckedElements(aForm, radioName) {
	if (FormUtil.getFormElementsLength(aForm, radioName) == 1) {
		if (aForm[radioName].checked) 
			return [aForm[radioName]];
		else 
			return [];
	}
	else {
		var ret = [];
		for (var i = 0; i < aForm[radioName].length; i++) {
			if (aForm[radioName][i].checked) ret[ret.length] = aForm[radioName][i];
		}
		return ret;
	}
}

function getCheckedRadioValue(name) {
	return $("input[@name="+name+"][@type=radio][@checked]").val();
}

/**
 *判断单选按钮是否选中
 *
 */
function checkRadio(aForm,radioName) {
	if (FormUtil.getFormValue(aForm, radioName)==null){
		alert('请选择单选按钮!');
		return false;
	}
 	return true;
}
/**
 * 选中（或取消选中）所有CheckBox
 */
function toggleAllCheckBox(aForm, chkName) {
	var chks = aForm.elements[chkName];
	if (aForm.getAttribute("__toggleAllCheckBox_"+chkName+"_flag__") == "true") {
		for (var i = 0; i < chks.length; i++) {
			if(chks[i].style.display == 'none') continue; 
			chks[i].checked = false;
		}
		aForm.setAttribute("__toggleAllCheckBox_"+chkName+"_flag__", "false");
	}
	else {
		for (var i = 0; i < chks.length; i++) {
			if(chks[i].style.display == 'none') continue; 
			chks[i].checked = true;
		}
		aForm.setAttribute("__toggleAllCheckBox_"+chkName+"_flag__", "true");
	}
}

SelectUtil = {
	selectValue : function(selector, value) {
		for (var i = 0; i < selector.options.length; i++) {
			if (selector.options[i].value == value) selector.options[i].selected = true;
		}
	}
}

function hiddenRemarkIfEmpty() {
	if (document.getElementsBySelector && document.getElementsBySelector(".remark ul").length <= 0) 
		document.getElementsBySelector(".remark")[0].style.display = "none";
}

function forcusOnFormInput() {
	for (var j = 0; j < document.forms.length; j++) {
		var theForm = document.forms[j];
		for (var i = 0; i < theForm.elements.length; i++) {
			var e = theForm.elements[i];
			if (e && (e.type && e.type.toLowerCase() != 'hidden' && e.style && e.style.display != 'none' )) {
				try {
					e.select();
					e.focus();
					return;
				}
				catch(e){}
			}
		}
	}
}

