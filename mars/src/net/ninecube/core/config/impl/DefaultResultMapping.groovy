package net.ninecube.core.config.impl;

import net.ninecube.core.config.*;
import net.ninecube.core.config.impl.*;

public class DefaultResultMapping implements ResultMapping {
	 
	/**
	 * 交易结果配置
	 * 共五项，前四项为条件，最后一项为结果视图代码
	 * 配置含义依次为：
	 * 交易名称  交易类型  动作名称  结果代码
	 * 结果代码的含义如下，空格被忽略：
	 * T：交易名称
	 * A：动作名称
	 * R：结果代码
	 * M：模型名称
	 * E：/error
	 * S：/success
	 * D：/download
	 * >: 表示结果类型为chain
	 * ^: 表示结果类型为stream
	 * 判断优先级：
	 */
	def configs = [
	              ["", "", "", "", "T.A.R"],
	              ["", "", "", "success", "S"],
	              ["", "", "default", "success", "T"],
	              ["", "", "", "input", "T"],
	              ["", "", "", "error", "E"],
	              ["login", "", "submit", "input", "T"],
	              ["login", "", "logout", "input", "T"],
	              ["login", "", "submit", "success", "<main"],
	              ["", "create, read, update, delete", "default", "success", "M"],
	              ["", "create, read, update, delete", "back", "", ">query"],
	              ["", "create, update, delete", "submit", "success", "S"],
	              ["", "create, update, delete", "submit", "input", "M"],
	              ["", "query, reflect", "", "", "T"],
	              ["", "download", "", "success", "^"],
	              ["updateRule,readRule", "", "default", "", "rule"],
	              ]
	
	public String getResultView(TransactionConf trans, 
			ModelConf model, ActionConf action, String resultcode) {
		return getResultView(trans.name, "${trans.type}", model.name, action.name, resultcode)
	}
	
	public String getResultView(String transName, String transType, String entityName, 
			String action, String resultcode ) {
		// 查找最匹配的配置
		def matcher = [grade: -1, code: "" ]
		configs.each { ctrans, ctype, caction, cresultcode, ccode ->
			def grade = 0
			[ 			[ctrans, transName, 4],
			            [ctype, transType, 3],
			            [caction, action, 2], 
			            [cresultcode, resultcode, 1]
			].find { s1, s2, value ->
				if (s1 == "") grade += 0
				else if (s1.split(", ").find{x -> x == s2}) grade += value
				else grade = -1
				return grade == -1
			}
			if (grade >= matcher.grade) {
				matcher.grade = grade
				matcher.code = ccode
			}
		}
		// 转换配置为实际视图名称
		def map = [M: "${entityName}", T: "${transName}",
					A: "${action}", R: resultcode, S: "/success", E: "/error", D: "/download" ]
		def ret = "";
		matcher.code.each { ch ->
			ret += (map[ch]) ? map[ch] : (ch == " " ? "" : ch)
		}
		return ret
	}
	
	public static void main(String[] s) {
		def mapping = new DefaultResultMapping()

		assert "user" == mapping.getResultView("create", "create", "user", "default", "success")
		assert "/success" == mapping.getResultView("create", "create", "user", "submit", "success")
		assert ">query" == mapping.getResultView("create", "create", "user", "back", "success")
		assert "ruleentity" == mapping.getResultView("read", "read", "ruleentity", "default", "success")
		assert "ruleentity" == mapping.getResultView("update", "update", "ruleentity", "default", "success")
		assert "user" == mapping.getResultView("create", "create", "user", "submit", "input")
		println "ok."
	}
}
