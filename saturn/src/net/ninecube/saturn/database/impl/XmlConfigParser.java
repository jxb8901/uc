package net.ninecube.saturn.database.impl;

import java.util.List;

import net.ninecube.util.StringUtil;

import org.apache.log4j.Logger;
import org.dom4j.Element;

public class XmlConfigParser {
	private static final Logger log = Logger.getLogger(XmlConfigParser.class);
	
	public static void parse(DatabaseManagerImpl dbm, String configFileName) {
		loadDatabases(dbm, net.ninecube.util.XmlUtil.getConfigElement(configFileName, "-//ninecube//database1.0//EN", "database.dtd"));
	}
	
	@SuppressWarnings("unchecked")
	private static void loadDatabases(DatabaseManagerImpl dbm, Element e) {
		dbm.setType(e.attributeValue("type"));
		List<Element> ch = e.elements();
		for (Element e1 : ch) {
			if ("database".equals(e1.getName())) {
				dbm.getDatabases().add(loadDatabase(e1));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static DatabaseImpl loadDatabase(Element e){
		DatabaseImpl ret = new DatabaseImpl();
		ret.setName(e.attributeValue("name"));
		ret.setType(e.attributeValue("type"));
		ret.setVersion(e.attributeValue("version"));
		ret.setUrl(e.attributeValue("url"));
		ret.setPort(e.attributeValue("port"));
		
		List<Element> ch = e.elements();
		for (Element e1 : ch) {
			if ("table".equals(e1.getName())) {
				ret.addTable(loadTable(e1));
			}
			else if ("view".equals(e1.getName())) {
				ret.addTable(loadView(e1));
			}
		}
		
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	private static TableImpl loadTable(Element e) {
		TableImpl ret = loadTableAttribute(new TableImpl(), e);
		
		List<Element> ch = e.elements();
		for (Element e1 : ch) {
			if ("column".equals(e1.getName())) {
				ret.addColumn(loadColumn(e1, ret));
			}
		}
		
		return ret;
	}
	
	private static TableView loadView(Element e) {
		TableView ret = loadTableAttribute(new TableView(), e);
		ret.setTableName(e.attributeValue("table"));
		ret.setWhereSql(e.getText().trim());
		return ret;
	}
	
	private static <T extends TableImpl> T loadTableAttribute(T table, Element e) {
		table.setName(e.attributeValue("name"));
		table.setAlias(StringUtil.split(e.attributeValue("alias")));
		table.setCentric("true".equals(e.attributeValue("centric")));
		return table;
	}
	
	private static ColumnImpl loadColumn(Element e, TableImpl table) {
		ColumnImpl ret = new ColumnImpl();
		ret.setName(e.attributeValue("name"));
		ret.setAlias(StringUtil.split(e.attributeValue("alias")));
		if (!StringUtil.isEmpty(e.attributeValue("sqltype")))
			ret.setSqlType(ColumnImpl.SqlType.valueOf(e.attributeValue("sqltype")));
		if (!StringUtil.isEmpty(e.attributeValue("type")))
			ret.setType(ColumnImpl.Type.valueOf(e.attributeValue("type")));
		ret.setValue(e.attributeValue("value"));
		ret.setFormula(e.attributeValue("formula"));
		ret.setEnumer(e.attributeValue("enumer"));
		ret.setPrimaryKey("true".equals(e.attributeValue("primarykey")));
		ret.setDateColumn("true".equals(e.attributeValue("datecolumn")));
		ret.setAssociateTable(StringUtil.split(e.attributeValue("asstable")));
		
		return ret;
	}
}
