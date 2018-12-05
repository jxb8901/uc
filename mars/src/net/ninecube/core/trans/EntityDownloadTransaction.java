/**
 * 
 * created on 2007-1-21
 */
package net.ninecube.core.trans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author jxb
 * 
 */
public class EntityDownloadTransaction extends EntityReadTransaction {
	private static final Log log = LogFactory.getLog(EntityDownloadTransaction.class);
	private int contentLength = -1;
	private InputStream inputStream;
	
	private void prepareData() throws Exception {
		byte[] data = (byte[]) PropertyUtils.getProperty(this.getModel(), this.getParameter("name"));
		this.inputStream = new ByteArrayInputStream(data);
		this.contentLength = data.length;
	}
	
	public InputStream getInputStream() throws Exception {
		if (contentLength == -1) prepareData();
		return inputStream;
	}
	
	public int getContentLength() throws Exception {
		if (contentLength == -1) prepareData();
		return contentLength;
	}
	
	public String getContentType() {
		try {
			return (String) PropertyUtils.getProperty(this.getModel(), this.getParameter("name") + "ContentType");
		} catch (Exception e) {
			return "application/x-msdownload; charset=UTF-8";
		} 
	}
	
	public String getFileName() {
		try {
			return (String) PropertyUtils.getProperty(this.getModel(), this.getParameter("name") + "FileName");
		} catch (Exception e) {
			return "venus.download";
		} 
	}
	
}
