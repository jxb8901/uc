/*
 * Created on 2004-3-15
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package net.ninecube.core.webwork.interceptor;

import java.io.File;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.webwork.dispatcher.multipart.MultiPartRequestWrapper;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.ActionSupport;
import com.opensymphony.xwork.interceptor.Interceptor;

/**
 * @author jxb
 */
public class FileUploadInterceptor implements Interceptor {
	protected static final Log log = LogFactory.getLog(FileUploadInterceptor.class);

	protected String allowedTypes;

	protected String disallowedTypes;

	protected Long maximumSize;

	public FileUploadInterceptor() {
		if (log.isDebugEnabled())
			log.debug("new FileUploadInterceptor()");
	}

	public void init() {
		if (log.isDebugEnabled())
			log.debug("FileUploadInterceptor.init()");
	}

	public void destroy() {
		if (log.isDebugEnabled())
			log.debug("FileUploadInterceptor.destroy()");
	}

	/**
	 * list of allowed mime-types, optional
	 */
	public void setAllowedTypes(String allowedTypes) {
		this.allowedTypes = allowedTypes;
	}

	/**
	 * list of diallowed mime-types, optional
	 */
	public void setDisallowedTypes(String disallowedTypes) {
		this.disallowedTypes = disallowedTypes;
	}

	/**
	 * maximum file Size, optional
	 */
	public void setMaximumSize(Long maximumSize) {
		this.maximumSize = maximumSize;
	}

	/**
	 * @TODO: 去除警告
	 */
	@SuppressWarnings(value={"unchecked", "deprecation"})
	public String intercept(ActionInvocation invocation) throws Exception {
		if (!(ServletActionContext.getRequest() instanceof MultiPartRequestWrapper)) {
			if (log.isDebugEnabled())
				log.debug("bypass " + invocation.getProxy().getNamespace() + "/" + invocation.getProxy().getActionName());
			return invocation.invoke();
		}
		MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) ServletActionContext.getRequest();
		if (multiWrapper.hasErrors()) {
			Collection errors = multiWrapper.getErrors();
			Iterator i = errors.iterator();
			while (i.hasNext()) {
				addError(invocation, (String) i.next());
			}
		}

		Enumeration e = multiWrapper.getFileParameterNames(); // Bind allowed
																// Files
		while (e.hasMoreElements()) {
			// get the value of this input tag
			String inputName = (String) e.nextElement();
			// get the content type
			String contentType = multiWrapper.getContentType(inputName);
			// get the name of the file from the input tag
			String fileName = multiWrapper.getFilesystemName(inputName);
			// Get a File object for the uploaded File
			File file = multiWrapper.getFile(inputName);
			log.info("file " + inputName + " " + contentType + " " + fileName + " " + file); // If
																								// it's
																								// null
																								// the
																								// upload
																								// failed
			if (file == null) {
				log.error("Error uploading: " + fileName);
			} else {
				if (acceptFile(invocation, file, contentType, inputName)) {
					log.debug("set parameter: " + inputName);
					invocation.getInvocationContext().getParameters().put(inputName, file);
					invocation.getInvocationContext().getParameters().put("fileName", fileName);
				}
				// Do additional processing/logging...
			}
		} // Invoke Action
		String result = invocation.invoke(); // Cleanup
		e = multiWrapper.getFileNames();
		while (e.hasMoreElements()) {
			String inputValue = (String) e.nextElement();
			File file = multiWrapper.getFile(inputValue);
			if (file != null && file.isFile()) {
				boolean deleted = file.delete();
				log.info("removing file " + inputValue + "[" + file + "]: " + (deleted ? "success" : "failed"));
			}
		}
		return result;
	}

	// overload this method to modify accept behaviour
	protected boolean acceptFile(ActionInvocation invocation, File file, String contentType, String inputName) {
		if (log.isDebugEnabled())
			log.debug("checking" + inputName + " " + file.getName() + " " + file.length() + " " + contentType);
		if (maximumSize != null && maximumSize.longValue() < file.length())
			addError(invocation, "file is too long:" + inputName + " " + file.getName() + " " + file.length());
		else if (allowedTypes != null && allowedTypes.indexOf(contentType) < 0)
			addError(invocation, "Content-Type not allowed:" + inputName + " " + file.getName() + " " + contentType);
		else if (disallowedTypes != null && disallowedTypes.indexOf(contentType) >= 0)
			addError(invocation, "Content-Type disallowed:" + inputName + " " + file.getName() + " " + contentType);
		// somehow we need to set error messages here...
		else {
			if (log.isDebugEnabled())
				log.debug("accepted");
			return true;
		}
		if (log.isDebugEnabled())
			log.debug("not accepted");
		return false;
	}

	protected void addError(ActionInvocation invocation, String msg) {
		Object action = invocation.getAction();
		if (action instanceof ActionSupport) {
			((ActionSupport) action).addActionError(msg);
		} else {
			// how to get to addError() from here?
			log.error(msg);
		}
	}
}
