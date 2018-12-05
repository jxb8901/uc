package net.ninecube.saturn.sql;


public interface SQLExecutor {

	public void execute(String sql);

	public void setAutoCommit(Boolean autoCommit);

	public void commit();

}
