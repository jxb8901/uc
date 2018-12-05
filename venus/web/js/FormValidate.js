
String.prototype.trim=function(){
     return this.replace(/^\s+|\s+$/gm,'');
};

function amountCompare(amount1, amount2) {
	var a1 = parseFloat(amount1);
	var a2 = parseFloat(amount2);
	if (a1 == a2) return 0;
	return a1 > a2 ? 1 : -1;
}

FormUtil = {
	
	initForm : function(theForm) {
		if (!theForm.fields || theForm.fields.length <= 0) return;
		for (var i = 0; i < theForm.fields.length; i++) {
			var field = theForm.fields[i];
			if (this.needValidate(theForm, field)) {
				this.__initField(theForm, field);
			}
		}
		for (var i = 0; i < theForm.fields.length; i++) {
			if (this.focusField(theForm, theForm.fields[0].name)) break;
		}
	},
	
	__initField : function(theForm, field) {
		if (field.require) {
			this.markFieldRequire(theForm, field.name);
		}
		if (field.format == 'integer') var keypressFunction = this.mustInteger;
		if (field.format == 'amount') var keypressFunction = this.mustAmount;
		if (keypressFunction) {
			if (this.getFormElementsLength(theForm, field.name) > 1) { //控件数组
				var ele = theForm[field.name][0];
			}
			else { 
				var ele = theForm[field.name]; 
			}
			if (ele && (ele.type && ele.type == 'text' || ele.tagName && ele.tagName == 'textarea')) {
				//@ TODO: 在firefox中不起作用？？
				this.attatchEvent(ele, "keypress", keypressFunction);
			}
		}
	},
	
	validate : function(theForm, confirmPrompt) {
		this.clearAllErrors(theForm);
		
		if (theForm.getAttribute("submitFlag") == "submitted") {
			FormUtil.addCommonError(theForm, "不能重复提交交易！");
		}
		
		var result;
		
		if (FormValidator.validate(theForm)) {
			if (typeof(checkInput) == "function") {
				result = checkInput(theForm);
			}
		}
		
		result = !(theForm.common_errors && theForm.common_errors.length > 0 ||
				theForm.field_errors && theForm.field_errors.length > 0);
		
		if (result) {
			if (theForm.confirmPrompt) result = confirm(theForm.confirmPrompt);
			if (result) this.encryptPassword(theForm);
			
			if (result && theForm.signature) { //数据签名
				var ret = sign(theForm);
				if (ret != "success") {
					FormUtil.addCommonError(theForm, ret);
					result = false;
				}
			}
		}
		
		if (!result) {
			this.showErrorMessages(theForm);
		}
		
		if (result) {
			theForm.setAttribute("submitFlag", "submitted");
		}
		else {
			if (theForm.getAttribute("submitFlag") != "submitted") {
				theForm.setAttribute("submitFlag", "nosubmit");
			}
		}
		
		return result;
	},
	
	getFormValue : function(theForm, fieldName) {
		if (!theForm[fieldName]) {
			alert("未定义字段："+fieldName);
			return null;	
		}
		if (this.getFormElementsLength(theForm, fieldName) > 1) { //控件数组
			if (theForm[fieldName][0].type != 'radio' && theForm[fieldName][0].type != 'checkbox') {
				for (var i = 0; i < theForm[fieldName].length; i++) {
					if (!theForm[fieldName][i].disabled) return theForm[fieldName][i].value.trim();
				}
				alert("定义了多个相同名称的有效控件，系统不能取得字段值："+fieldName);
				return null;
			}
			else {
				for (var i = 0; i < theForm[fieldName].length; i++) {
					//对于checkbox，只返回了第一个被选中的值，是否应该返回一个值数组呢？
					if (theForm[fieldName][i].checked) return theForm[fieldName][i].value.trim();
				}
				return null;
			}
		}
		if ((theForm[fieldName].type == 'radio' || theForm[fieldName].type == 'checkbox') &&
				!theForm[fieldName].checked) {
			return null;
		}
		return theForm[fieldName].value.trim();
	},
	
	isVisible : function(theForm, fieldName) {
		if (theForm[fieldName] && (theForm[fieldName].type == 'hidden' ||
				theForm[fieldName].style && theForm[fieldName].style.display == 'none' )) {
			return false;
		}
		return true;
	},
	
	getFormElementsLength : function(theForm, fieldName) {
		var count = 0;
		for (var i = 0; i < theForm.elements.length; i++) {
			if (theForm.elements[i].name == fieldName) count++;
		}
		return count;
	},
	
	needValidate : function(theForm, rule) {
		if (!theForm['do']) { alert("未定义do隐藏字段"); return false; }
		var ac = theForm['do'].value || 'default';
		
		//默认为所有非默认动作执行校验
		if (!rule.action) {
			return "default" != ac; 
		}
		else {
			if ("any" == rule.action) return true;
			return rule.action.indexOf(ac) != -1;
		}
	},
	
	addCommonError : function(theForm, msg) {
		if (!theForm.common_errors) theForm.common_errors = [];
		theForm.common_errors[theForm.common_errors.length] = msg;
	},
	
	addFieldError : function(theForm, fieldName, msg) {
		if (!theForm.field_errors) theForm.field_errors = [];
		theForm.field_errors[theForm.field_errors.length] = {name:fieldName, message:msg}
	},
	
	showErrorMessages : function(theForm, errorMode) {
		if (errorMode == 'alert') this.alertErrorMessage(theForm);
		this.markErrorMessage(theForm);
	},
	
	alertErrorMessage : function(theForm) {
		
	},
	
	markErrorMessage : function(theForm) {
		if (theForm.common_errors && theForm.common_errors.length > 0) {
			this.markCommonErrors(theForm);
		}
		if (theForm.field_errors && theForm.field_errors.length > 0) {
			for (var i = 0; i < theForm.field_errors.length; i++) {
				this.markFieldError(theForm, theForm.field_errors[i].name, theForm.field_errors[i].message);
			}
		}
		if (theForm.field_errors && theForm.field_errors.length > 0) {
			for (var i = 0; i < theForm.field_errors.length; i++) {
				if (this.focusField(theForm, theForm.field_errors[0].name)) break;
			}
		}
	},
	
	focusField : function(theForm, fieldName) {
		if (theForm[fieldName]) {
			try {
				theForm[fieldName].select();
				theForm[fieldName].focus();
				return true;
			}
			catch(e){return false;}
		}
		return false;
	},
	
	markCommonErrors : function(theForm) {
		this.getCommonErrorPanel(theForm).innerHTML = "<div class='error'>" + theForm.common_errors.join("\r\n") + "</div>";
		if (this.getCommonErrorPanel(theForm).parentNode.style.display == "none") {
			this.getCommonErrorPanel(theForm).parentNode.style.display = "";
		}
	},
	
	markFieldError : function(theForm, fieldName, msg) {
		this.getFieldErrorPanel(theForm, fieldName).innerHTML = msg;
	},
	
	markFieldRequire : function(theForm, fieldName) {
		if (this.isVisible(theForm, fieldName))
			this.getFieldRequirePanel(theForm, fieldName).innerHTML = "*";
	},
	
	clearAllErrors : function(theForm){
		if (theForm.common_errors && theForm.common_errors.length > 0) {
			this.clearCommonErrors(theForm);
		}
		if (theForm.field_errors && theForm.field_errors.length > 0) {
			for (var i = 0; i < theForm.field_errors.length; i++) {
				this.clearFieldError(theForm, theForm.field_errors[i].name);
			}
			theForm.field_errors = null;
		}
	},
	
	clearCommonErrors : function(theForm) {
		theForm.common_errors = [];
		this.getCommonErrorPanel(theForm).innerHTML = "";
	},
	
	clearFieldError : function(theForm, fieldName) {
		this.getFieldErrorPanel(theForm, fieldName).innerHTML = "";
	},
	
	getCommonErrorPanel : function(theForm) {
		var span = document.getElementById("error_msg");
		if (span) return span;
		span = document.createElement("DIV");
		span.id = "error_msg";
		span.style.color = "red";
		theForm.appendChild(span);
		return span;
	},
	
	getFieldErrorPanel : function(theForm, fieldName) {
		var id = "__ErrorMessagePanel_"+fieldName;
		var span = document.getElementById(id);
		if (span) return span;
		try{
			var span = document.createElement("SPAN");
			span.id = id;
			span.style.color = "red";
			this.getFieldParentNode(theForm, fieldName).appendChild(span);
			return span;
		}
		catch(e){alert(fieldName+":"+e.description);}
	},
	
	getFieldRequirePanel : function(theForm, fieldName) {
		var id = "__RequirePanel__"+fieldName;
		var span = document.getElementById(id);
		if (span) return span;
		try{
			var span = document.createElement("SPAN");
			span.id = id;
			span.style.color = "red";
			this.getFieldParentNode(theForm, fieldName).appendChild(span);
			return span;
		}
		catch(e){alert(fieldName+":"+e.description);}
	},
	
	getFieldParentNode : function(theForm, fieldName) {
		if (this.getFormElementsLength(theForm, fieldName) > 1) { //控件数组
			for (var i = 0; i < theForm[fieldName].length; i++) {
				if (!theForm[fieldName][i].disabled) return theForm[fieldName][i].parentNode;
			}
			return theForm[fieldName][0].parentNode;
		}
		return theForm[fieldName].parentNode;
	},
	
	encryptPassword : function(theForm) {
		if (theForm.pk) {
			for (var i = 0; i < 10; i++) {
				var index = (i <= 0) ? "" : ""+i;
				if (theForm.elements["Password"+index] && this.getFormValue(theForm, "Password"+index)){
					theForm.elements["Password"+index].value = this.encrypt(theForm.pk, theForm.elements["Password"+index].value);
				}
			}
		}
	},
	
	encrypt : function(key, str) {
		return des(key, str, true);
	},
	
	mustAmount : function(ev) {
		if (!ev) ev = window.event;
		var key = FormUtil.__getKeyCode(ev);
		var ret = (key >= 45 && key <= 57);
		if (!ret)  ev.returnValue = false;
		return ret;
	},
	
	mustInteger : function(ev) {
		if (!ev) ev = window.event;
		var key = FormUtil.__getKeyCode(ev);
		var ret = (key >= 48 && key <= 57);
		if (!ret)  ev.returnValue = false;
		return ret;
	},
	
	__getKeyCode : function(ev) {
		if (!ev) return true;
		if (ev.keyCode) var key = ev.keyCode;
		if (ev.which) var key = ev.which;
		return key;
	},
	
	attatchEvent : function(el, eventName, func) {
		if (document.addEventListener) {  // DOM Level 2 Event Model
		    el.addEventListener(eventName, func, true);
		}
		else if (document.attachEvent) {  // IE 5+ Event Model
		    el.attachEvent("on"+eventName, func);
		}
		else {  // IE 4 Event Model
		    el["on"+eventName] = func;
		}
	}

}

FormValidator = {
	theForm: document.forms[0],
	validate_result: true,
	
	validate : function(theForm){
		this.theForm = theForm;
		this.resetResult();
		this.vlaidate_result = true;
		
		if (!this.theForm.fields && !this.theForm.expressions) return this.vlaidate_result;

		if (this.theForm.fields) {
			for (var i = 0; i < this.theForm.fields.length; i++) {
				if (FormUtil.needValidate(this.theForm, this.theForm.fields[i])) this.validateField(this.theForm.fields[i]);
			}
		}
		if (this.theForm.expressions) {
			for (var i = 0; i < this.theForm.expressions.length; i++) {
				if (FormUtil.needValidate(this.theForm, this.theForm.expressions[i])) this.validateExpression(this.theForm.expressions[i]);
			}
		}
		
		return this.vlaidate_result;
	},
	
	resetResult : function(){
		if (this.theForm.fields) {
			for (var i = 0; i < this.theForm.fields.length; i++) {
				this.theForm.fields[i].result = null;
			}
		}
		if (this.theForm.expressions) {
			for (var i = 0; i < this.theForm.expressions.length; i++) {
				this.theForm.expressions[i].result = null;
			}
		}
	},
	
	getFieldValue : function(field) {
		return FormUtil.getFormValue(this.theForm, field.name);
	},
	
	getFieldByName : function(name) {
		if (!this.theForm.fields) return null;
		for (var i = 0; i < this.theForm.fields.length; i++) {
			if (this.theForm.fields[i].name == name) return this.theForm.fields[i];
		}
		return null;
	},
	
	getExpressionByName : function(name) {
		if (!this.theForm.expressions) return null;
		for (var i = 0; i < this.theForm.expressions.length; i++) {
			if (this.theForm.expressions[i].name == name) return this.theForm.expressions[i];
		}
		return null;
	},
	
	validateExpression : function(expression) {
		if (this.checkExpressionDepends(expression)) {
			if (!eval(expression.expression)) this.addExpressionError(expression);
		}
	},
	
	checkExpressionDepends : function(expression) {
		if (!expression.depends) return true;
		var depends = expression.depends.split(",");
		for (var i = 0; i < depends.length; i++) {
			var field = this.getFieldByName(depends[i]);
			if (field && field.result == "error") return false;
			var ex = this.getExpressionByName(depends[i]);
			if (ex && ex.result == "error") return false; //@ TODO: 没有检查表达式是否已执行
		}
		return true;
	},
	
	validateField : function(field) {
		if (field.require) this.validateRequire(field);
		if (!this.getFieldValue(field)) return;
		if (field.result != 'error') this.validateLengthRange(field);
		if (field.result != 'error') this.validateFormat(field);
		if (field.result != 'error') this.validateValueRange(field);
	},
	
	validateRequire : function(field) {
		if (!field.require) return;
		if (!this.getFieldValue(field)) {
			this.addFieldError(field, "必填");
		}
	},
	
	validateLengthRange : function(field) {
		if (!field.maxlen && !field.minlen && !field.len) return;
		if (field.maxlen && field.minlen) {
			if (this.getFieldValue(field).length > parseInt(field.maxlen) || 
					this.getFieldValue(field).length < parseInt(field.minlen))
				this.addFieldError(field, "长度必须在"+field.minlen+"~"+field.maxlen+"之间");
		}
		else if (field.minlen) {
			if (this.getFieldValue(field).length < parseInt(field.minlen))
				this.addFieldError(field, "长度必须大于等于"+field.minlen);
		}
		else if (field.maxlen) {
			if (this.getFieldValue(field).length > parseInt(field.maxlen))
				this.addFieldError(field, "长度必须小于等于"+field.maxlen);
		}
		else if (field.len) {
			if (this.getFieldValue(field).length != parseInt(field.len)) 
				this.addFieldError(field, "长度必须为"+field.len);
		}
	},
	
	validateFormat : function(field) {
		if (!field.format) return;
		switch (field.format) {
			case 'string' : 
				if (!this.isString(this.getFieldValue(field), field.pattern)) 
					this.addFieldError(field, "格式错误");
				break;
			case 'date' : 
				if (!this.isDate(this.getFieldValue(field), field.pattern)) 
					this.addFieldError(field, "日期格式错误");
				break;
			case 'amount' : 
				if (!this.isAmount(this.getFieldValue(field), field.pattern)) 
					this.addFieldError(field, "金额格式错误");
				break;
			case 'integer' : 
				if (!this.isInteger(this.getFieldValue(field), field.pattern))
					this.addFieldError(field, "只能输入数字");
				break;
			case 'postcode' : 
				if (!this.isPostcode(this.getFieldValue(field), field.pattern)) 
					this.addFieldError(field, "邮编格式错误");
				break;
			case 'phone' : 
				if (!this.isPhone(this.getFieldValue(field), field.pattern)) 
					this.addFieldError(field, "电话格式错误");
				break;
			case 'mobile' : 
				if (!this.isMobile(this.getFieldValue(field), field.pattern)) 
					this.addFieldError(field, "手机格式错误");
				break;
			case 'email' : 
				if (!this.isEmail(this.getFieldValue(field), field.pattern)) 
					this.addFieldError(field, "电子邮件格式错误");
				break;
			case 'password' : 
				if (!this.isPassword(this.getFieldValue(field), field.pattern)) 
					this.addFieldError(field, "密码太简单或有非法字符");
				break;
			case 'en_char' :
			    if (!this.isEn_char(this.getFieldValue(field), field.pattern)) 
					this.addFieldError(field, "有非英语字母");
				break;
			default : 
				this.addFieldError(field, "不支持的数据格式："+field.format);
		}
	},
	
	validateValueRange : function(field) {
		if (!field.minvalue && !field.maxvalue) return;
		if (field.minvalue && field.maxvalue) {
			var maxvalue = (field.format == 'amount') ? parseFloat(field.maxvalue) : field.maxvalue;
			var minvalue = (field.format == 'amount') ? parseFloat(field.minvalue) : field.minvalue;
			if (this.getFieldValue(field) > maxvalue || this.getFieldValue(field) < minvalue)
				this.addFieldError(field, field.longname+"必须在"+minvalue+"~"+maxvalue+"之间");
		}
		else if (field.minvalue) {
			var minvalue = (field.format == 'amount') ? parseFloat(field.minvalue) : field.minvalue;
			if (this.getFieldValue(field) < minvalue)
				this.addFieldError(field, field.longname+"必须大于等于"+minvalue);
		}
		else {
			var maxvalue = (field.format == 'amount') ? parseFloat(field.maxvalue) : field.maxvalue;
			if (this.getFieldValue(field) > maxvalue)
				this.addFieldError(field, field.longname+"必须小于等于"+maxvalue);
		}
	},
	
	addFieldError : function(field, msg){
		this.vlaidate_result = false;
		field.result = "error";
		FormUtil.addFieldError(this.theForm, field.name, field.msg ? field.msg : msg);
	},
	
	addExpressionError : function(expression){
		this.vlaidate_result = false;
		expression.result = "error";
		if (expression.fieldName && this.theForm[expression.fieldName])
			FormUtil.addFieldError(this.theForm, expression.fieldName, expression.msg);
		else 
			FormUtil.addCommonError(this.theForm, expression.msg);
	},
	
	isString : function(val, pattern) {
		return pattern.test(val);
	},
	
	isEmail : function(val, pattern) {
		return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(val);
	},
	
	isMobile : function(val, pattern) {
		return /^\d{10,14}$/.test(val);
	},
	
	isPhone : function(val, pattern) {
		return /^[\d\-]{6,10}$/.test(val);
	},
	
	isPostcode : function(val, pattern) {
		return /^[1-9]\d{5}$/.test(val);
	},
	
	isInteger : function(val, pattern) {
		return /^\d+$/.test(val);
	},
	
	isAmount : function(val, pattern) {
		return /^\d+(\.\d{0,2})?$/.test(val);
	},
	
	isPassword : function(val, pattern) {
		return /^.{6,10}$/.test(val) && !(val in ['123456','1234567','12345678','asdfgh','qwerty','zxcvbn']);
	},
	isEn_char : function(val, pattern) {
		return /([a-z][A-Z])+/.test(val);
	},
	isIdCard : function(number){
		var date, Ai;
		var verify = "10x98765432";
		var Wi = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
		var area = ['','','','','','','','','','','','北京','天津','河北','山西','内蒙古','','','','','','辽宁','吉林','黑龙江','','','','','','','','上海','江苏','浙江','安微','福建','江西','山东','','','','河南','湖北','湖南','广东','广西','海南','','','','重庆','四川','贵州','云南','西藏','','','','','','','陕西','甘肃','青海','宁夏','新疆','','','','','','台湾','','','','','','','','','','香港','澳门','','','','','','','','','国外'];
		var re = number.match(/^(\d{2})\d{4}(((\d{2})(\d{2})(\d{2})(\d{3}))|((\d{4})(\d{2})(\d{2})(\d{3}[x\d])))$/i);
		if(re == null) return false;
		if(re[1] >= area.length || area[re[1]] == "") return false;
		if(re[2].length == 12){
			Ai = number.substr(0, 17);
			date = [re[9], re[10], re[11]].join("-");
		}
		else{
			Ai = number.substr(0, 6) + "19" + number.substr(6);
			date = ["19" + re[4], re[5], re[6]].join("-");
		}
		if(!this.IsDate(date, "ymd")) return false;
		var sum = 0;
		for(var i = 0;i<=16;i++){
			sum += Ai.charAt(i) * Wi[i];
		}
		Ai += verify.charAt(sum%11);
		return (number.length ==15 || number.length == 18 && number == Ai);
	},
	
	isDate : function(op, formatString){
		var m, year, month, day;
		//只支持yyyyMMdd格式
		m = op.match(new RegExp("^(\\d{4})(\\d{2})(\\d{2})$"));
		if(m == null ) return false;
		day = m[3];
		month = m[2];
		year = m[1];
		var date = new Date(year, month-1, day);
		return (typeof(date) == "object" && year == date.getFullYear() && month == (date.getMonth()+1) && day == date.getDate());
	}
};
