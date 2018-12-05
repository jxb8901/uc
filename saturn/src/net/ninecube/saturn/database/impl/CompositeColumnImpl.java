package net.ninecube.saturn.database.impl;

import java.util.List;

import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.CompositeColumn;

public class CompositeColumnImpl extends ColumnImpl implements CompositeColumn {

	private List<Column> innerColumns;
	
	public CompositeColumnImpl(List<Column> innerColumns){
		this.innerColumns = innerColumns;
	}
	
	public CompositeColumnImpl() {
		
	}

	public List<Column> getColumns() {		
		return innerColumns;
	}

	public List<Column> getInnerColumns() {
		return innerColumns;
	}

	public void setInnerColumns(List<Column> innerColumns) {
		this.innerColumns = innerColumns;
	}

}
