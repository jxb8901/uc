package net.ninecube.core.webwork;

import com.opensymphony.xwork.validator.ValidatorConfig;
import net.ninecube.core.webwork.*
import net.ninecube.core.config.*
import net.ninecube.core.config.impl.*

class ValidatorAdaptorImpl implements ValidatorAdaptor {
	 
	public List<ValidatorConfig> adapt(FieldConf fc) {
		def ret = [] 
		def i = 0
		if (fc.req) {
			if ("string" == "${fc.type}")
				ret[i++] = new ValidatorConfig(type:"requiredstring", 
						params: ["fieldName":fc.name, "trim":"true"], 
						defaultMessage:"必须输入[${fc.cname}]")
			else
				ret[i++] = new ValidatorConfig(type:"required", 
						params: ["fieldName":fc.name], 
						defaultMessage:"必须输入[${fc.cname}]")
		}
		if (fc.len > 0 || fc.maxlen > 0 || fc.minlen > 0) {
			if (fc.len > 0) {
				def vc = new ValidatorConfig(type:"stringlength", 
						params: ["fieldName":fc.name])
				vc.params["maxLength"] = "${fc.len}"
				vc.params["minLength"] = "${fc.len}"
				vc.defaultMessage = "字段[${fc.cname}]长度必须为${fc.len}"
				ret[i++] = vc
			}
			else {
				if (fc.maxlen > 0) {
					def vc = new ValidatorConfig(type:"stringlength", 
							params: ["fieldName":fc.name])
					vc.defaultMessage = "字段[${fc.cname}]长度必须小于${fc.maxlen}"
					vc.params["maxLength"] = "${fc.maxlen}"
					ret[i++] = vc
				}
				if (fc.minlen > 0) {
					def vc = new ValidatorConfig(type:"stringlength", 
							params: ["fieldName":fc.name])
					vc.defaultMessage = "字段[${fc.cname}]长度必须大于${fc.minlen}"
					vc.params["minLength"] = "${fc.minlen}"
					ret[i++] = vc
				}
			}
		}
		if (""+fc.type in ["integer", "amount", "date"]) {
			def regex = ["integer": '[0-9]+', "amount": '[0-9]+(\\.[0-9]{0,2}', "date": '[12][90][0-9]{2}[01][0-9][0-3][0-9]']
			def msg = ["integer": "数字格式不正确[${fc.cname}]", "amount": "金额格式不正确[${fc.cname}]", "date": "日期格式不正确[${fc.cname}]" ]
			ret[i++] = new ValidatorConfig(type: "regex", 
					params: ["fieldName":fc.name, "expression": regex["${fc.type}"]], 
					defaultMessage:msg["${fc.type}"])
			
			if (fc.maxvalue || fc.minvalue) {
				def type = ["integer":"int", "amount":"double", "date":"date"]
				def vc = new ValidatorConfig(type: type["${fc.type}"], 
						params: ["fieldName":fc.name], 
						defaultMessage:"字段[${fc.cname}]必须"  )
				if (fc.maxvalue) {
					vc.params["${fc.type}"=="amount"? "maxInclusive": "max"] = fc.maxvalue
					vc.defaultMessage += "小于${fc.maxvalue}"
				}
				if (fc.minvalue) {
					vc.params["${fc.type}"=="amount"? "minInclusive": "min"] = fc.minvalue
					vc.defaultMessage += "大于${fc.minvalue}"
				}
				ret[i++] = vc
			}
		}
		if (""+fc.type in ["url", "email"]) {
			def msg = ["url": "URL格式不正确[${fc.cname}]", "email": "EMail格式不正确[${fc.cname}]"]
			ret[i++] = new ValidatorConfig(type: "${fc.type}", 
					params: ["fieldName":fc.name], 
					defaultMessage:msg["${fc.type}"])
		}
		if (fc.pattern) {
			ret[i++] = new ValidatorConfig(type: "regex", 
					params: ["fieldName":fc.name, expression:fc.pattern], 
					defaultMessage:(fc.patternmsg?fc.patternmsg:"格式不正确")+"[${fc.cname}]")
		}
		
		return ret
	}
	
	public ValidatorConfig adapt(ExpressionConf ec) {
		if (ec.fieldName)
			return new ValidatorConfig(type: "fieldexpression", 
					params: ["fieldName":ec.name, expression: ec.java], 
					defaultMessage: ec.errormsg)
		else
			return new ValidatorConfig(type: "expression", 
					params: [expression: ec.java], 
					defaultMessage: ec.errormsg)
	}

	static void main(args) {
		  [
		   /* 测试数据格式：每行由一个map和一个list组成，map用于创建fieldconf或expressionconf，list用于检验结果验证器类型及数量 */
	          	 [[req:false], []],
		          [[req:true], ["requiredstring"]],
		          [[req:true, type:FieldType.integer], ["required", "regex"]],
		          [[len:5], ["stringlength"]],
		          [[maxlen:5], ["stringlength"]],
		          [[minlen:5], ["stringlength"]],
		          [[maxlen:5, minlen:5], ["stringlength","stringlength"]],
		          [[type:FieldType.integer, maxvalue:5], ["regex", "int"]],
		          [[type:FieldType.amount, maxvalue:5], ["regex", "double"]],
		          [[type:FieldType.date, maxvalue:5], ["regex", "date"]],
		          [[type:FieldType.url], ["url"]],
		          [[type:FieldType.email], ["email"]], 
		          [[pattern:"[0-9][d-f]", patternmsg:"必须为一位数字加一位字符"], ["regex"]],
		          [[req:true, type:FieldType.integer, maxvalue: "5", len:10], ["required", "stringlength", "regex", "int"]],
	    ].each {x, y ->
	 		print "${x.type}, FieldConf: ${x} excepted: ${y}"
			def va = new ValidatorAdaptorImpl()
		 	def fc = new FieldConfImpl(x)
		 	def vc = va.adapt(fc)
	 		println ", actual: ${vc*.type}"
		 	assert y.size() == vc.size()
		 	assert y == vc*.type
		 }
		  
		  def va = new ValidatorAdaptorImpl()
		  assert "expression" == va.adapt(new ExpressionConf()).type
		  def ev = va.adapt(new ExpressionConf(fieldName:"f1", errormsg:"MSG"));
		  assert "fieldexpression" == ev.type
		  assert "MSG" == ev.defaultMessage
	}
}
