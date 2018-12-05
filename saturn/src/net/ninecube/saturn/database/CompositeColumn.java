package net.ninecube.saturn.database;

import java.util.List;

public interface CompositeColumn extends Column {

	public List<Column> getColumns();

}
