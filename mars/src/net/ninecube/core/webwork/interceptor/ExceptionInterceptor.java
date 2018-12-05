/*
 * Created on 2004-3-18
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.ninecube.core.webwork.interceptor;

import java.util.Iterator;
import java.util.List;

import net.ninecube.lang.BaseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.ActionSupport;
import com.opensymphony.xwork.config.entities.ExceptionMappingConfig;
import com.opensymphony.xwork.interceptor.ExceptionHolder;
import com.opensymphony.xwork.interceptor.ExceptionMappingInterceptor;
import com.opensymphony.xwork.interceptor.Interceptor;

/**
 * 处理Action异常的Interceptor
 * 
 * @author jxb
 */
public class ExceptionInterceptor extends ExceptionMappingInterceptor {
	protected static final Log log = LogFactory.getLog(ExceptionInterceptor.class);
	/** 未明确定义异常处理器的异常所要应用的Result */
	private String exceptionResult;
	
	public void setExceptionResult(String result) {
		this.exceptionResult = result;
	}

	public String intercept(ActionInvocation invocation) throws Exception {
		String result;

		try {
			result = invocation.invoke();
		} catch (Exception e) {
			if (logEnabled) {
				handleLogging(e);
			}
			List exceptionMappings = invocation.getProxy().getConfig().getExceptionMappings();
			String mappedResult = this.findResultFromExceptions(exceptionMappings, e);
			if (mappedResult != null) {
				result = mappedResult;
				publishException(invocation, new ExceptionHolder(e));
			/* hack begin: 增加对异常转换为错误信息的处理 */
			} else if (e instanceof BaseException && invocation.getAction() instanceof ActionSupport) {
				((ActionSupport) invocation.getAction()).addActionError(e.getMessage());
				return Action.INPUT;
			} else if (this.exceptionResult != null){
				result = this.exceptionResult;
				publishException(invocation, new ExceptionHolder(e));
			/* hack end */
			} else {
				throw e;
			}
		}

		return result;
	}

	/**
	 * copy from ExceptionMappingInterceptor#findResultFromExceptions
	 */
	private String findResultFromExceptions(List exceptionMappings, Throwable t) {
		String result = null;

		// Check for specific exception mappings.
		if (exceptionMappings != null) {
			int deepest = Integer.MAX_VALUE;
			for (Iterator iter = exceptionMappings.iterator(); iter.hasNext();) {
				ExceptionMappingConfig exceptionMappingConfig = (ExceptionMappingConfig) iter.next();
				int depth = getDepth(exceptionMappingConfig.getExceptionClassName(), t);
				if (depth >= 0 && depth < deepest) {
					deepest = depth;
					result = exceptionMappingConfig.getResult();
				}
			}
		}

		return result;
	}

	public String intercept1(ActionInvocation invocation) throws Exception {
		try {
			if (log.isDebugEnabled()) {
				log.debug("ExceptionInterceptor.intercept()...");
			}
			return invocation.invoke();
		} catch (BaseException e) {
			log.info("Action处理出现异常:" + invocation.getProxy().getActionName(), e);
			((ActionSupport) invocation.getAction()).addActionError(e.getMessage());
			return Action.INPUT;
		}
	}
}
