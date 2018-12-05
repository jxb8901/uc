package net.ninecube.saturn.database

import java.util.*

import net.ninecube.saturn.database.*
import net.ninecube.saturn.database.impl.*
import net.ninecube.saturn.database.util.*
import net.ninecube.saturn.enumer.*
import net.ninecube.test.*

public class TestDataGenerator {
	public def static long customer_size = 1000
	public def static long trans_size = 10000
	public def static long seqno = 0
	def em = EnumerManager.get()
	def r = { end, start = 1 ->  (int)(Math.random() * (end - start) + start) }
	def d = { end, start, month_end = 12, month_start = 1 -> r(end, start)+"${r(month_end, month_start)}".padLeft(2, "0")+"${r(28)}".padLeft(2, "0") }
	def custno = {no ->
		def type = no % 2 == 0 || no % 3 == 0 ? 1 : 8;
		return "${type}" + "${no}".padLeft(9, "0")
	}
	def columnTemplateByAlias = [
     		"基本客户信息.客户号" : { custno(seqno) }, 
     		"个人客户信息.ID" : { custno(seqno) }, 
     		"个人客户信息.出生日期" : { d(1990, 1950) }, 
     		"交易.ID" : { seqno }, 
     		"交易.交易账号" : { custno(r(customer_size+1)) + "${r(10)}".padLeft(6, "0") }, 
     		"交易.日期" : { d(2007, 2007, 6, 1)}
         ]
 	def columnTemplateByType = [
 	        "dyna" : { d(2008, 2004) }, 
 	     ]
 	def columnTemplateByEnumer = { c -> def enumer = [] + em.getValues(c.enumer).keySet(); enumer[r((enumer).size(), 0)] }
	
	def columnTemplateBySqlType = [
			"date" : { d(2008, 2004) }, 
			"time" : { "${r(24)}:00:00".padLeft(8, "0") }, 
			"decimal" : { r(99) * 10  }, 
			"varchar" : { c -> "${c.alias[0]}" + seqno }, 
         ]	

	def generateDataForColumn(column) {
		def ret = null
		if (column.formula != null) return null;
		columnTemplateByAlias.each { alias, generator ->
			def names = alias.split("\\.")
			if (column.owner.hasAlias("${names[0]}") && column.hasAlias("${names[1]}")) {
				ret = generator(column)
			}
		}
		if (ret == null && column.enumer != null)
			ret = columnTemplateByEnumer(column)
		if (ret == null && columnTemplateByType["${column.type}"] != null)
			ret = columnTemplateByType["${column.type}"](column)
		if (ret == null && columnTemplateBySqlType["${column.sqlType}"] != null)
			ret = columnTemplateBySqlType["${column.sqlType}"](column)
		return "NULL".equals(ret) ? null : ret
	}
	
	def generateInsertForTable(table) {
		def ret = []
   		if (table instanceof List) {
			table.each { t -> ret << generateInsertForTable(t)[0]; seqno--; }
			seqno++
		}else if (table instanceof TableView) {
	
		}
		else {
			def columns = []
			def values = []
			seqno++
			table.columns.each { column -> 
				def value = generateDataForColumn(column)
				if (value != null) {
					columns << column.name
					values << value
				}
			}
			ret << "insert into ${table.name}(" + columns.join(", ") +
					") values('" + values.join("', '") + "'); "
		}
		return ret;
	}
	
	def generateDeleteForTable(table) {
		def ret = []
       if (table instanceof List) {
  			table.each { t -> ret << generateDeleteForTable(t)[0] }
  		}
  		else if (table instanceof TableView) {
   			
   		}
   		else {
   			ret << "delete from ${table.name};"
   		}
   		return ret;
	}
	
	def getTables() {
		def ret = []
   		ret << [customer_size, DatabaseManager.get().getTableByAlias("客户") ]
   		ret << [trans_size, DatabaseManager.get().getTableByAlias("交易").get(0) ] 
   		return ret;
	}
	
	def generateData() {
		def con = new SetupDatabase().getConnection()
		def sqlexecutor = {ss -> 
			def sqls = []
			ss.each {sql -> sqls << sql.toString() }
			//println(sqls)
			SetupDatabase.executeSql(con, sqls)
		}
		getTables().each { number, table ->
			seqno = 0
			sqlexecutor(generateDeleteForTable(table))
			number.times {
				sqlexecutor(generateInsertForTable(table))
			}
		}
		con.close()
	}
	
	public static void main(String[] a) throws Exception {
		new TestDataGenerator().generateData()
	}
}