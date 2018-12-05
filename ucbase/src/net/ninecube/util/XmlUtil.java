/**
 * 
 * created on 2007-3-16
 */
package net.ninecube.util;

import java.io.IOException;
import java.io.InputStream;

import net.ninecube.lang.BaseException;
import net.ninecube.lang.TypedMap;
import net.ninecube.util.internal.XStreamUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.collections.MapConverter;

/**
 * 
 * @author jxb
 * 
 */
public class XmlUtil {
	private static final Log log = LogFactory.getLog(XmlUtil.class);

	public static String obj2xml(Object o) {
		return XStreamUtil.obj2xml(o);
	}

	public static Object xml2obj(String s) {
		return XStreamUtil.xml2obj(s);
	}

	public static Element getConfigElement(final String configFileName, final String publicId, final String dtdFileName) {
		Document doc = null;
		InputStream is = null;

		is = XmlUtil.class.getResourceAsStream("/"+configFileName);
		if (is == null) throw new BaseException("can't open config file: '/" + configFileName + "'");

		SAXReader reader = new SAXReader(true);
		reader.setEntityResolver(new EntityResolver() {
			public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
				if (publicId.equals(publicId)) {
					java.io.InputStream is = this.getClass().getResourceAsStream("/" + dtdFileName);
					if (is != null) return new InputSource(is);
				}
				throw new BaseException("can't open dtd file: '/" + dtdFileName + "'");
			}
		});
		
		try {
			doc = reader.read(is);
		} catch (DocumentException e) {
			throw new BaseException(e);
		} finally {
			try {
				if (is != null) is.close();
			} catch (IOException e) {
				log.error("Unable to close input stream", e);
			}
		}
		
		return doc.getRootElement();
	}
}
