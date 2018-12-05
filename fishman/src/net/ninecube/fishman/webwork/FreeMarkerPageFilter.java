/**
 * 
 * created on 2007-1-18
 */
package net.ninecube.fishman.webwork;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ninecube.fishman.parser.ParseException;
import net.ninecube.fishman.sitemesh.FishManPage;
import net.ninecube.fishman.sitemesh.FishManUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.filter.PageResponseWrapper;
import com.opensymphony.webwork.views.freemarker.FreemarkerManager;
import com.opensymphony.xwork.ActionContext;

import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

/**
 * 
 * @author jxb
 * 
 */
public class FreeMarkerPageFilter extends com.opensymphony.webwork.sitemesh.FreeMarkerPageFilter {
	private static final Log log = LogFactory.getLog(FreeMarkerPageFilter.class);

    @Override
	public void doFilter(ServletRequest rq, ServletResponse rs, FilterChain chain) throws IOException, ServletException {
    	if ("true".equals(rq.getParameter("debugsitemesh"))) chain.doFilter(rq, rs);
		super.doFilter(rq, rs, chain);
	}

	/**
     *  Applies the decorator, using the relevent contexts
     * 
     * @param page The page
     * @param decorator The decorator
     * @param req The servlet request
     * @param res The servlet response
     * @param servletContext The servlet context
     * @param ctx The action context for this request, populated with the server state
     */
    protected void applyDecorator(Page page, Decorator decorator,
                                  HttpServletRequest req, HttpServletResponse res,
                                  ServletContext servletContext, ActionContext ctx)
            throws ServletException, IOException {
        try {
            FreemarkerManager fmm = FreemarkerManager.getInstance();

            // get the configuration and template
            Configuration config = fmm.getConfiguration(servletContext);
            Template template = config.getTemplate(decorator.getPage(), getLocale(ctx.getActionInvocation(), config)); // WW-1181

            PageResponseWrapper pageResponse = new PageResponseWrapper(res, FishManUtil.getFactory(getFilterConfig()));
            // get the main hash
            SimpleHash model = fmm.buildTemplateModel(ctx.getValueStack(), null, servletContext, req, pageResponse, config.getObjectWrapper());
            // finally, render it
            template.process(model, pageResponse.getWriter());
            
            if (pageResponse.getPage() == null) {
            	log.warn("取不到模板页，可能是模板文件中未调用response.setContentType："+decorator.getPage());
            	FishManUtil.writeOriginal(req, res, page);
            }
            else {
            	FishManUtil.process(req, res, (FishManPage)pageResponse.getPage(), (FishManPage)page);
            }
        } catch (ParseException e) {
        	throw e; // TODO: 这里抛出的异常信息为什么是乱码？
        } catch (Exception e) {
            String msg = "Error applying decorator: " + e.getMessage();
        	log.debug(msg, e);
            throw new ServletException(msg, e);
        }
    }

	@Override
	@SuppressWarnings("deprecation")
	public FilterConfig getFilterConfig() {
        return super.getFilterConfig();
    }
    
}
