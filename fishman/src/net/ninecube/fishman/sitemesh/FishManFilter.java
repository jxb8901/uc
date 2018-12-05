/**
 * 
 * created on 2007-1-17
 */
package net.ninecube.fishman.sitemesh;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.Factory;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.filter.PageFilter;
import com.opensymphony.module.sitemesh.filter.PageResponseWrapper;
import com.opensymphony.module.sitemesh.util.Container;

/**
 * 
 * @author jxb
 * 
 */
public class FishManFilter extends PageFilter {

	@Override
	protected void applyDecorator(Page page, Decorator decorator, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
            request.setAttribute(PAGE, page);
            ServletContext context = getFilterConfig().getServletContext();
            // see if the URI path (webapp) is set
            if (decorator.getURIPath() != null) {
                // in a security conscious environment, the servlet container
                // may return null for a given URL
                if (context.getContext(decorator.getURIPath()) != null) {
                    context = context.getContext(decorator.getURIPath());
                }
            }
            // get the dispatcher for the decorator
            RequestDispatcher dispatcher = context.getRequestDispatcher(decorator.getPage());
            // create a wrapper around the response
            PageResponseWrapper pageResponse = new PageResponseWrapper(response, getFactory());
            dispatcher.include(request, pageResponse);
            Page template = pageResponse.getPage();
            FishManUtil.process(request, response, (FishManPage)template, (FishManPage)page);

            // set the headers specified as decorator init params
            while (decorator.getInitParameterNames().hasNext()) {
                String initParam = (String) decorator.getInitParameterNames().next();
                if (initParam.startsWith("header.")) {
                    response.setHeader(initParam.substring(initParam.indexOf('.')), decorator.getInitParameter(initParam));
                }
            }

            request.removeAttribute(PAGE);
        }
        catch (RuntimeException e) {
            // added a print message here because otherwise Tomcat swallows
            // the error and you never see it = bad!
            if (Container.get() == Container.TOMCAT)
                e.printStackTrace();

            throw e;
        }
	}

	protected Factory getFactory() {
		return Factory.getInstance(new Config(getFilterConfig()));
	}

	@Override
	@SuppressWarnings("deprecation")
	public FilterConfig getFilterConfig() {
        return super.getFilterConfig();
    }
}
