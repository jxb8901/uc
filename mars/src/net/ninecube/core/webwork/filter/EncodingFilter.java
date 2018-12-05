/*
 * Created on 2004-3-15
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.ninecube.core.webwork.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author jxb
 */
public class EncodingFilter implements Filter {
	protected static final Log log = LogFactory.getLog(EncodingFilter.class);

	/**
	 * 
	 */

	public void init(FilterConfig arg0) throws ServletException {
	}

	/**
	 * 
	 */

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		request.setCharacterEncoding("UTF-8");
		chain.doFilter(request, response);
	}

	/**
	 * 
	 */

	public void destroy() {
	}
}
