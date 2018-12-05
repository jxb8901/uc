/*
 * Created on 2004-3-15
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.ninecube.core.webwork.result;

import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.Result;

/**
 * @author jxb
 * @deprecated use StreamResult
 */
public class WordResult implements Result {
	// ~ Instance fields
	// ////////////////////////////////////////////////////////

	InputStream docFileStream;

	String fileName;

	// ~ Methods
	// ////////////////////////////////////////////////////////////////

	public void setDocFileStream(InputStream s) {
		this.docFileStream = s;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void execute(ActionInvocation invocation) throws Exception {

		if (docFileStream == null) {
			docFileStream = (InputStream) invocation.getStack().findValue("docFileStream");
		}

		if (docFileStream == null) {
			throw new NullPointerException("No doc file found");
		}

		if (fileName == null) {
			fileName = (String) invocation.getStack().findValue("fileName");
		}

		if (fileName == null) {
			fileName = "document.doc";
		}

		HttpServletResponse response = ServletActionContext.getResponse();
		// set headers to disable caching
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		if (fileName.endsWith(".doc")) {
			response.setContentType("application/vnd.ms-word");
		} else if (fileName.endsWith(".pdf")) {
			response.setContentType("application/pdf");
		} else if (fileName.endsWith(".xls")) {
			response.setContentType("application/vnd.ms-excel");
		}
		response.setHeader("Content-disposition", "inline; filename=" + fileName);

		try {
			OutputStream os = response.getOutputStream();
			int b;
			while ((b = docFileStream.read()) != -1) {
				os.write(b);
			}
			os.flush();
		} finally {
			docFileStream.close();
		}
	}
}
