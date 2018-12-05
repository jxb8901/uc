package net.ninecube.saturn.database

import net.ninecube.saturn.database.*;
import net.ninecube.saturn.database.impl.*;
import net.ninecube.db.*;
import net.ninecube.test.*;

public class SchemaGenerator {
	def sql = [];
	def databaseType = "mysql"
	def generated = [:]
	
	def generate(database) {
		database.getTables().each{ table -> generateTable(table); }
	}
	
	def generateTable(table) {
		if (table instanceof TableView) return
		if (generated[table.name]) return
		generated[table.name] = true
		def fieldsql = table.getColumns().collect { field ->  
		generateField(field); }.findAll { e -> e != null }
		fieldsql = fieldsql.join(", \n")
		
		sql << "drop table  if exists  ${table.name};"
		sql << """CREATE TABLE ${table.name} (
${fieldsql},
primary key (${table.primaryKey.name})
);
""";
	}
	
	def generateField(field) {
		if (field.formula != null) {
			return null
		}
		if ("integer" == "${field.sqlType}") {
			return "${field.name} int"
		}
		if ("autoint" == "${field.sqlType}") {
			if (databaseType == "hsql")
				return "${field.name} bigint identity"
			else
				return "${field.name} bigint auto_increment"
		}
		else if ("decimal" == "${field.sqlType}") {
			return "${field.name} decimal(10, 2)"
		}
		else if ("date" == "${field.sqlType}") {
			return "${field.name} date";
		}
		else if ("datetime" == "${field.sqlType}") {
			return "${field.name} datetime";
		}
		else if (field.len > 0) {
			return "${field.name} varchar(${field.len})";
		}
		else {
			return "${field.name} varchar(255)";
		}
		return null
	}
	
	public static List<String> generateDatabase(Database database) {
		def generator = new SchemaGenerator()
		generator.generate(database)
		return generator.sql
	}
	
	public static List<String> generateAll() {
		generateAll("mysql");
	}
	
	public static List<String> generateAll(String databaseType) {
		def generator = new SchemaGenerator()
		generator.databaseType = databaseType;
		DatabaseManager.get().getDatabases().each { database ->
			generator.generate(database)
		}
		return generator.sql
	}
	
	public static void createDB() {
		createDB(new SetupDatabase());
	}
	
	public static void createDB(SetupDatabase setup) {
		def sqls = []
		generateAll(setup.getJdbcDriver().contains("hsqldb") ? "hsql" : "mysql").each {
			sql -> sqls << sql.toString()
		}
		setup.executeSql(sqls);
	}
	
	public static void main(String[] a) throws Exception {
		println generateAll()
		//createDB()
	}
}