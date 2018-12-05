package net.ninecube.saturn.database

import net.ninecube.saturn.database.*
import net.ninecube.saturn.database.impl.*

public class TableImplTestFixture {
	
	public DatabaseManagerImpl dbm
	public TableImpl cust_base
	public TableImpl cust_info
	public TableView pos
	public TableImpl trans
	public TableImpl pointCal
	public TableImpl pointAccount
	public TableImpl sms
	public TableImpl formulaValue
	
	public void setUp() {
		dbm = new DatabaseManagerImpl()
		cust_base = new TableImpl(name: "cust_base", alias: ["customer","客户", "客户基本信息"], centric: true,
				columns : [new ColumnImpl(name : "id" , alias : ["客户号"], primaryKey : true),
				           new ColumnImpl(name : "name" , alias : ["姓名"]),
				           new ColumnImpl(name : "sex" , alias : ["性别"])
						])
		cust_info = new TableImpl(name: "cust_info", alias: ["customer","客户", "客户详细信息"], centric: true,
				columns : [new ColumnImpl(name : "id" , alias : ["客户号"], associateTable: ["客户基本信息.客户号"], primaryKey : true),
				           new ColumnImpl(name : "date" , alias : ["出生日期"], type : ColumnImpl.Type.dyna, dateColumn: true),
				           new ColumnImpl(name : "name" , alias : ["姓名"]),
				           new ColumnImpl(name : "sex" , alias : ["性别"])
						])
		trans = new TableImpl(name: "trans", alias: ["交易", "交易记录"],
				columns : [new ColumnImpl(name : "id" , alias : ["流水号"], primaryKey : true),
				           new ColumnImpl(name : "custid" , alias : ["客户号"], associateTable: ["客户基本信息.客户号", "客户详细信息.客户号"]),
				           new ColumnImpl(name : "amount" , alias : ["金额"]),
				           new ColumnImpl(name : "transdate", alias : ["交易日期"], type : ColumnImpl.Type.dyna, dateColumn: true),
				           new ColumnImpl(name : "transtype", alias : ["交易类型","交易渠道"], enumer:"servtp"),
		           		    new ColumnImpl(name : "transmonth", alias : ["交易月份"], formula : "substring(transdate,   4,2)")
						])
		pointCal = new TableImpl(name: "POINTCAL", alias: ["POINTCAL", "积分计算表"],
				columns : [new ColumnImpl(name : "id" , alias : ["流水号"], primaryKey : true),
				           new ColumnImpl(name : "CUSTOMERID" , alias : ["客户号"], associateTable: ["客户基本信息.客户号", "客户详细信息.客户号"]),
				           new ColumnImpl(name : "TRANSTYPE" , alias : ["交易类型"]),
				           new ColumnImpl(name : "TRANSID", alias : ["交易流水号"]),
				           new ColumnImpl(name : "POINTTYPE", alias : ["积分类型"]),
		           		    new ColumnImpl(name : "POINT", alias : ["POINT"]),
		           		    new ColumnImpl(name : "COMMENTS", alias : ["COMMENTS"]),
		           		    new ColumnImpl(name : "PROMOTIONPLANID", alias : ["PROMOTIONPLANID"]),
		         		    new ColumnImpl(name : "TRANSTIME", alias : ["积分产生日期"])
						])
		pointAccount = new TableImpl(name: "POINTACCOUNT", alias: ["POINTACCOUNT", "客户积分"],
				columns : [new ColumnImpl(name : "id" , alias : ["流水号"], primaryKey : true),
				           new ColumnImpl(name : "CUSTOMERID" , alias : ["客户号"], associateTable: ["客户基本信息.客户号", "客户详细信息.客户号"]),
				           new ColumnImpl(name : "POINTTYPE", alias : ["积分类型"]),
		           		    new ColumnImpl(name : "POINT", alias : ["消费积分", "POINT"])
						])
		sms = new TableImpl(name: "SMSBONUSCAL", alias: ["SMSBONUSCAL", "短信奖励"],
				columns : [new ColumnImpl(name : "id" , alias : ["流水号"], primaryKey : true),
				           new ColumnImpl(name : "CUSTOMERID" , alias : ["客户号"], associateTable: ["客户基本信息.客户号", "客户详细信息.客户号"]),
				           new ColumnImpl(name : "CONTENT", alias : ["内容"])
						])
		formulaValue = new TableImpl(name: "formulavalues", alias: ["指标值表", "formulavalues"],
				columns : [new ColumnImpl(name : "id" , alias : ["id"], primaryKey : true),
				           new ColumnImpl(name : "frequence" , alias : ["frequence"]),
				           new ColumnImpl(name : "date" , alias : ["date"]),
				           new ColumnImpl(name : "target_type", alias : ["target_type"]),
				           new ColumnImpl(name : "target_id", alias : ["target_id"]),
		           		    new ColumnImpl(name : "target_date_id", alias : ["target_date_id"]),
		           		    new ColumnImpl(name : "formula_id", alias : ["formula_id"]),
		           		    new ColumnImpl(name : "dimension_type", alias : ["dimension_type"]),
		           		    new ColumnImpl(name : "dimension", alias : ["dimension"]),
		         		    new ColumnImpl(name : "formula_value", alias : ["formula_value"])
						])
		pos = new TableView(name: "POS", whereSql : "[交易渠道] = 'POS'", tableName : "trans")
		
		dbm.setDatabases([new DatabaseImpl(name: "mysql").addTable(cust_base).addTable(cust_info), 
		                  new DatabaseImpl(name: "sqlserver").addTable(trans).addTable(formulaValue)
		                  .addTable(pointCal).addTable(pointAccount).addTable(sms)])
		dbm.databases.each{db -> db.postProcess(dbm)}
	}
}
