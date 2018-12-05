/**
 * 
 * created on 2007-3-29
 */
package net.ninecube.core.webwork.util;

import java.io.IOException;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.webwork.views.freemarker.FreemarkerManager;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * 
 * @author jxb
 * 
 */
public class FreemarkerUtil {

	public static boolean isTemplateExists(String name) throws TemplateException, IOException {
		Configuration config = FreemarkerManager.getInstance().getConfiguration(ServletActionContext.getServletContext());
		return config.getTemplateLoader().findTemplateSource(name) != null;
	}
}
