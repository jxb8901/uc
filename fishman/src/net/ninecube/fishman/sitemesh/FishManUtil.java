/**
 * 
 * created on 2007-1-18
 */
package net.ninecube.fishman.sitemesh;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ninecube.fishman.FishMan;
import net.ninecube.fishman.InvalidDocException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.Factory;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.RequestConstants;

/**
 * 
 * @author jxb
 * 
 */
public class FishManUtil {
	private static final Log log = LogFactory.getLog(FishManUtil.class);

	public static void process(HttpServletRequest request, HttpServletResponse response,
			FishManPage template, FishManPage instance) throws IOException {
		if (log.isDebugEnabled()) log.debug("merge template, template size="+template.getData().length + ",instance size=" + instance.getData().length);
		writeOriginal(request, response, mergeTemplate(template, instance));
	}

	public static FishManPage mergeTemplate(FishManPage template, FishManPage instance) throws IOException {
		try {
			Reader ret = FishMan.process(new CharArrayReader(template.getData()), new CharArrayReader(instance.getData()));
			StringBuffer sb = new StringBuffer(4096);
			int ch = -1;
			while ((ch = ret.read()) != -1) sb.append((char)ch);
			ret.close();
			return new FishManPage(sb.toString().toCharArray());
		} catch (InvalidDocException e) {
			if (e.isTemplate()) log.error("template is invalid, use orignal content!", e);
			return instance;
		}
	}

	public static Factory getFactory(FilterConfig config) {
		return Factory.getInstance(new Config(config));
	}

    /** Write the original page data to the response. */
	/**
	 * @see super{@link #writeOriginal(HttpServletRequest, HttpServletResponse, Page)}
	 */
    public static void writeOriginal(HttpServletRequest request, HttpServletResponse response, Page page) throws IOException {
    	// page.getContentLength() 需要将char[]转换为byte[]再取长度，可能会有不必要的资源消耗
    	// 在tomcat下不调用response.setContentLength，页面也能正常显示，应该对性能有所好处
    	// 其它容器尚未做测试，不知HTTP规范对此是否有要求
        //response.setContentLength(page.getContentLength());
        if (isUsingStream(request, response))
        {
            PrintWriter writer = new PrintWriter(response.getOutputStream());
            page.writePage(writer);
            //flush writer to underlying outputStream
            writer.flush();
            response.getOutputStream().flush();
        }
        else
        {
            page.writePage(response.getWriter());
            response.getWriter().flush();
        }
    }
    
    private static boolean isUsingStream(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	boolean ret = request.getAttribute(RequestConstants.USING_STREAM).equals(Boolean.TRUE);
    	if (ret) {
			try {
				// Sitemesh设置的标志有可能不准确，实际测试一番
				response.getOutputStream();
			} catch (IllegalStateException e) {
				return false;
			}
    	}
    	return ret;
    }
}
