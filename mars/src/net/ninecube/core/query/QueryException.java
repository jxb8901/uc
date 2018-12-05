/*
 * Created on 2004-7-16
 *
 */
package net.ninecube.core.query;

import net.ninecube.lang.BaseException;

/**
 * @author jxb
 */
public class QueryException extends BaseException
{
	/**
	 *
	 */
	public QueryException()
	{
		super();
	}

	/**
	 *
	 */
	public QueryException(String arg0)
	{
		super(arg0);
	}

	/**
	 *
	 */
	public QueryException(Throwable arg0)
	{
		super(arg0);
	}

	/**
	 *
	 */
	public QueryException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}
}
