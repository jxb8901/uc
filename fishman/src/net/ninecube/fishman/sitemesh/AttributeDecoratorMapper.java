/**
 * 
 * created on 2007-1-19
 */
package net.ninecube.fishman.sitemesh;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.DecoratorMapper;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.mapper.AbstractDecoratorMapper;

/**
 * 根据request中的属性决定应用哪个修饰器，属性名在sitemesh.xml中用
 * "decorator.attribute.name"定义，默认为“decorator”
 * 另外还可以定义空修饰器的名称（默认为“none”）， 如果指定修饰器的名称
 * 为该空修改器名称，则不修饰页面
 * @author jxb
 * 
 */
public class AttributeDecoratorMapper extends AbstractDecoratorMapper {
	private static final Log log = LogFactory.getLog(AttributeDecoratorMapper.class);
	    private String decoratorAttributeName = null;
	    private String decoratorNoneName = null;

	    public void init(Config config, Properties properties, DecoratorMapper parent) throws InstantiationException {
	        super.init(config, properties, parent);
	        decoratorAttributeName = properties.getProperty("decorator.attribute.name", "decorator");
	        decoratorNoneName = properties.getProperty("decorator.none.name", "none");
	    }

	    public Decorator getDecorator(HttpServletRequest request, Page page) {
	        Decorator result = null;
	        String decoratorName = (String)request.getAttribute(decoratorAttributeName);

	        if (decoratorNoneName.equals(decoratorName)) return null;
	        if (decoratorName != null && !decoratorName.trim().equals("")) {
	                result = getNamedDecorator(request, decoratorName);
	        }
	        return result == null ? super.getDecorator(request, page) : result;
	    }
}
