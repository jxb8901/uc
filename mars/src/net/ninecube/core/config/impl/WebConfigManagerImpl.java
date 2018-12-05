/**
 * 
 * created on 2006-12-15
 */
package net.ninecube.core.config.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.ninecube.core.config.ConfigException;
import net.ninecube.core.config.ExpressionConf;
import net.ninecube.core.config.FieldTagType;
import net.ninecube.core.config.FieldType;
import net.ninecube.core.config.TransactionConf;
import net.ninecube.core.config.TransactionType;
import net.ninecube.core.config.WebConfig;
import net.ninecube.core.config.WebConfigManager;
import net.ninecube.core.config.field.EnumFieldConf;
import net.ninecube.core.config.field.ExcludeFieldsConf;
import net.ninecube.core.config.field.FieldConfListImpl;
import net.ninecube.core.config.field.IncludeFieldsConf;
import net.ninecube.core.config.field.MergeFieldsConf;
import net.ninecube.core.config.field.ModelFieldsConf;
import net.ninecube.core.config.field.QueryFieldsConfImpl;
import net.ninecube.core.query.Operator;
import net.ninecube.util.StringUtil;
import net.ninecube.util.XmlUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.opensymphony.util.FileManager;
import com.opensymphony.xwork.config.ConfigurationException;

/**
 * 
 * @author jxb
 * 
 */
public class WebConfigManagerImpl extends WebConfigManager {
	private static final Log LOG = LogFactory.getLog(WebConfigManagerImpl.class);

	private WebConfigImpl config;
	private Set<String> includedFileNames = new TreeSet<String>();
	private String configFileName = "venus.xml";
	
	public WebConfigManagerImpl() {
	}

	public WebConfigManagerImpl(String configFileName) {
		this.configFileName = configFileName;
	}

	public WebConfig getConfiguration() {
		return config;
	}

	public void init() {
		LOG.debug("初始化配置文件");
		includedFileNames.clear();

		LOG.debug("loading config: " + configFileName);
		Element root = getConfigElement(configFileName);
		config = new WebConfigImpl(root.attributeValue("appname"),
				root.attributeValue("appcname"));			
		loadConfig(root);
		config.postProcessAfterLoadConfig();
		WebConfigManager.setInstance(this);
		
		LOG.debug("load conf [" + config.getPackages().size() + "]");
	}

	public boolean needsReload() {
		boolean needsReload = FileManager.fileNeedsReloading(configFileName);
		Iterator fileNameIterator = includedFileNames.iterator();

		while (!needsReload && (fileNameIterator.hasNext())) {
			String fileName = (String) fileNameIterator.next();
			needsReload = FileManager.fileNeedsReloading(fileName);
		}

		return needsReload;
	}
	
	protected void loadConfig(Element root) {
		List ch = root.elements();
		for (Object o : ch) {
			if (!(o instanceof Element)) continue;
			Element e = (Element)o;
			if ("package".equals(e.getName())) {
				PackageConfImpl p = new PackageConfImpl(config, 
						e.attributeValue("name"), 
						e.attributeValue("cname"), 
						root.attributeValue("controllerbasedir"),
						root.attributeValue("viewbasedir"));
				
				loadPackage(e.elements(), p);
			}
			else if ("include".equals(e.getName())) {
				String fileName = e.attributeValue("file");
				Element include = getConfigElement(fileName);
				if (include != null) {
					loadConfig(include);
					if (LOG.isDebugEnabled()) {
						LOG.debug("Loaded configuration from: " + fileName);
					}
				}
			}
		}
	}
	
	private void loadPackage(List packageElements, PackageConfImpl parent) {
		List ch = packageElements;
		for (Object o : ch) {
			if (!(o instanceof Element)) continue;
			Element e = (Element)o;
			if ("package".equals(e.getName())) {
				PackageConfImpl p = new PackageConfImpl(parent, 
						e.attributeValue("name"), 
						e.attributeValue("cname"));
				loadPackage(e.elements(), p);
			}
			else if ("model".equals(e.getName())) {
				parent.setModel(loadModel(parent, e));
			}
			else if ("transaction".equals(e.getName())) {
				loadTransaction(e, parent);
			}
		}
	}

	private TransactionConf loadTransaction(Element transactionElement, PackageConfImpl parent) {
		TransactionConfImpl ret = new TransactionConfImpl(parent, 
				transactionElement.attributeValue("name"),
				transactionElement.attributeValue("cname"));
		ret.setInterceptors(StringUtil.split(transactionElement.attributeValue("interceptors")));
		ret.setInherit(StringUtil.split(transactionElement.attributeValue("inherit")));
		ret.setExtend(transactionElement.attributeValue("extend"));
		ret.setActionNames(StringUtil.split(transactionElement.attributeValue("actions")));
		if (!StringUtil.isEmpty(transactionElement.attributeValue("type"))) {
			ret.setType(TransactionType.valueOf(transactionElement.attributeValue("type")));
		}
		
		List ch = transactionElement.elements();
		for (Object o : ch) {
			if (!(o instanceof Element)) continue;
			Element e = (Element)o;
			if ("input".equals(e.getName())) {
				ret.setInput(loadFields(e));
			}
			else if ("output".equals(e.getName())) {
				ret.setOutput(loadFields(e));
			}
			else if ("expressions".equals(e.getName())) {
				loadExpressions(ret, e);
			}
			else if ("param".equals(e.getName())) {
				loadParam(ret, e);
			}
		}
		
		return ret;
	}

	private void loadParam(TransactionConfImpl tc, Element paramElement) {
		String value = paramElement.getTextTrim();
		if (StringUtil.isEmpty(value)) value = paramElement.attributeValue("value");
		tc.setParameter(paramElement.attributeValue("name"), value);
	}

	private ModelConfImpl loadModel(PackageConfImpl owner, Element fieldsElement) {
		ModelConfImpl ret = new ModelConfImpl(owner);
		String entity = fieldsElement.attributeValue("class");
		if (!StringUtil.isEmpty(entity)) {
			try {
				ret.setEntityClass(Class.forName(entity));
			} catch (ClassNotFoundException e1) {
				throw new ConfigException("can't find entity class: '" + entity + "'");
			}
		}
		ret.setName(fieldsElement.attributeValue("entity"));
		ret.setFields(loadFields(fieldsElement).getRawFields());
		return ret;
	}

	private FieldConfListImpl loadFields(Element fieldsElement) {
		FieldConfListImpl ret = new FieldConfListImpl();
		List ch = fieldsElement.elements();
		for (Object o : ch) {
			if (!(o instanceof Element)) continue;
			Element e = (Element)o;
			if ("field".equals(e.getName())) {
				ret.add(loadField(new FieldConfImpl(), e));
			}
			else if ("modelfields".equals(e.getName())) {
				ret.add(loadModelField(e));
			}
			else if ("enumfield".equals(e.getName())) {
				ret.add(loadEnumField(e));
			}
			else if ("includefields".equals(e.getName())) {
				ret.add(loadField(new IncludeFieldsConf(), e));
			}
			else if ("excludefields".equals(e.getName())) {
				ret.add(loadField(new ExcludeFieldsConf(), e));
			}
			else if ("mergefield".equals(e.getName())) {
				ret.add(loadField(MergeFieldsConf.newMerge(), e));
			}
			else if ("queryfield".equals(e.getName())) {
				ret.add(loadQueryField(e));
			}else{
				throw new IllegalArgumentException("Unrecognized field type : " + e.getName());
			}
		}
		return ret;
	}

	private <T extends FieldConfImpl> T loadField(T ret, Element e) {
		List<String> providedAttributeNames = new ArrayList<String>();
		for (Iterator it = e.attributeIterator(); it.hasNext(); ) {
			Attribute at = (Attribute) it.next();
			if (!StringUtil.isEmpty(e.attributeValue(at.getName()))) {
				providedAttributeNames.add(at.getName());
				
				if ("action".equals(at.getName())) {
					ret.setAction(StringUtil.split(e.attributeValue("action")));
				}
				else if ("type".equals(at.getName())) {
					ret.setType(FieldType.valueOf(e.attributeValue("type")));
				}
				else if ("tagtype".equals(at.getName())) {
					ret.setTagtype(FieldTagType.valueOf(e.attributeValue("tagtype")));
				}
				else {
					try {
						BeanUtils.setProperty(ret, at.getName(), e.attributeValue(at.getName()));
					} catch (Exception e1) {
						throw new ConfigException("failure to load field: " + 
								e.asXML() + ", field name: " + at.getName(), e1);
					}
				}
			}
		}
		if (!StringUtil.isEmpty(e.getTextTrim())) {
			providedAttributeNames.add("value");
			ret.setValue(e.getTextTrim());
		}
		
		ret.setMergeableFieldNames(providedAttributeNames);
		
		return ret;
	}

	private QueryFieldsConfImpl loadQueryField(Element e) {
		QueryFieldsConfImpl ret = new QueryFieldsConfImpl();
		
		loadField(ret, e);
		ret.setOperator(Operator.valueOf(e.attributeValue("op")));
		return ret;
	}

	private ModelFieldsConf loadModelField(Element e) {
		ModelFieldsConf ret = new ModelFieldsConf(config, Boolean.valueOf(e.attributeValue("multi")));
		
		loadField(ret, e);
		ret.setModelName(e.attributeValue("model"));
		return ret;
	}

	private EnumFieldConf loadEnumField(Element e) {
		EnumFieldConf ret = new EnumFieldConf();
		ret.setEnumname(e.attributeValue("enum"));
		ret.setMulti(Boolean.valueOf(e.attributeValue("multi")));
		ret.setElementtype(e.attributeValue("elementtype"));
		loadField(ret, e);
		return ret;
	}

	private void loadExpressions(TransactionConfImpl tc, Element expressionsElement) {
		List ch = expressionsElement.elements();
		for (Object o : ch) {
			if (!(o instanceof Element)) continue;
			Element e = (Element)o;
			if ("expression".equals(e.getName())) {
				tc.addExpression(loadExpression(e));
			}
		}
	}

	private ExpressionConf loadExpression(Element e) {
		ExpressionConf ret = new ExpressionConf();
		ret.setName(e.attributeValue("name"));
		ret.setCname(e.attributeValue("cname"));
		ret.setDepends(StringUtil.split(e.attributeValue("depends")));
		ret.setFieldName(e.attributeValue("fieldname"));
		ret.setErrormsg(e.attributeValue("errormsg"));
		ret.setAction(StringUtil.split(e.attributeValue("action")));
		ret.setJava(e.elementText("java"));
		ret.setJavascript(e.elementText("javascript"));
		
		return ret;
	}

	protected InputStream getInputStream(String fileName) {
		return FileManager.loadFile(fileName, this.getClass());
	}

	protected Element getConfigElement(String fileName) {
		if (includedFileNames.contains(fileName)) return null;
		includedFileNames.add(fileName);
		return XmlUtil.getConfigElement(fileName, "-//ninecube//venus1.0//EN", "venus.dtd");
	}
	
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof WebConfigManagerImpl)) {
			return false;
		}

		final WebConfigManagerImpl xmlConfigurationManager = (WebConfigManagerImpl) o;

		if ((configFileName != null) ? (!configFileName.equals(xmlConfigurationManager.configFileName)) : (xmlConfigurationManager.configFileName != null)) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		return ((configFileName != null) ? configFileName.hashCode() : 0);
	}
}
