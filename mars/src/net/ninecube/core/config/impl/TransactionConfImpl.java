/**
 * 
 * created on 2006-12-14
 */
package net.ninecube.core.config.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ninecube.core.config.ActionConf;
import net.ninecube.core.config.ExpressionConf;
import net.ninecube.core.config.TransactionConf;
import net.ninecube.core.config.TransactionType;
import net.ninecube.core.config.TransactionUtils;
import net.ninecube.core.config.WebConfig;
import net.ninecube.core.config.field.FieldConfListImpl;
import net.ninecube.core.config.field.IncludeFieldsConf;
import net.ninecube.core.config.field.MergeFieldsConf;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 
 * @author jxb
 * @TODO: 交易继承机制参考spring的bean继承机制
 */
public class TransactionConfImpl implements TransactionConf, PostProcess {
	private static final Log log = LogFactory.getLog(TransactionConfImpl.class);
	
	public static final String EXTEND_ALL = "all";
	public static final String EXTEND_RESULT = "result";
	public static final String EXTEND_VALIDATOR = "validator";
	public static final String EXTEND_CLASS = "class";
	
	private PackageConfImpl parent;
	private String name;
	private String cname;
	private TransactionType type;
	private List<String> actionNames;
	private TransactionConfImpl extendTransaction;
	private String extend;
	private List<String> inherit;
	private WebConfigImpl config;
	private List<String> interceptors;
	private Map<String, String> params = new HashMap<String, String>();
	private FieldConfListImpl input = new FieldConfListImpl();
	private FieldConfListImpl output = new FieldConfListImpl();
	private List<ExpressionConf> expressions = new ArrayList<ExpressionConf>();
	
	public TransactionConfImpl() { }
	
	public TransactionConfImpl(PackageConfImpl parent, String name, String cname) {
		this.parent = parent;
		this.name = name;
		this.cname = cname;
		this.config = parent.getConfig();
		this.parent.addTransaction(this);
	}
	
	public void postProcessAfterLoadConfig() {
		this.initFieldConfList(true);
		this.initFieldConfList(false);
	}
	
	private void initFieldConfList(boolean input) {
		FieldConfListImpl fields;
		if (input) {
			fields = this.input;
			// “reflect"交易的默认值不需要任何字段
			if (TransactionType.reflect == this.getType()) {
				
			}
			else if (TransactionType.query == this.getType()) {
				fields.addDefaultField(IncludeFieldsConf.DEFAULT_INPUT_INCLUDEALL);
				fields.addDefaultField(MergeFieldsConf.DEFAULT_INPUT_MERGEKEY);
			}
			else {
				// TODO: 这里的默认值应用于“查询”交易时，因为名称未作变换，
				// 会导致从新建或修改交易返回时，被修改的记录的值被带入查询条件中，影响显示
				fields.addDefaultField(IncludeFieldsConf.DEFAULT_INPUT_INCLUDEALL);
				fields.addDefaultField(MergeFieldsConf.DEFAULT_INPUT_MERGEKEY);
			}
		}
		else {
			fields = this.output;
			fields.addDefaultField(IncludeFieldsConf.DEFAULT_OUTPUT_INCLUDEALL);
			fields.addDefaultField(MergeFieldsConf.DEFAULT_OUTPUT_MERGEKEY);
		}
		fields.resolve(this.parent.getModel(), input);
	}
	
	//~  
	
	public boolean isValidExtend() {
		if (this.getExtendTransaction() == null) return false;
		if (this.getExtendTransaction() == this) {
			log.error("发生交易互相继承，该继承无效："+this.getName()+","+this.getExtendTransaction().getName());
			return false;
		}
		return true;
	}
	
	public boolean isInherit(String item) {
		if (this.isValidExtend()) {
			if (this.inherit.contains(EXTEND_ALL) ||
					this.inherit.contains(item)) {
				return true;
			}
		}
		return false;
	}
	
	//~ 
	
	public String getNamespace() {
		return this.parent.getNamespace();
	}
	
	public String getTransactionClassName() {
		if (this.isInherit(EXTEND_CLASS)) {
			return getExtendTransaction().getTransactionClassName();
		}
		return parent.getJavaPackageName() + "." + StringUtils.capitalize(this.name);
	}

	public String getFullName() {
		return this.parent.getFullName() + "." + this.name;
	}

	public ActionConfImpl getAction(String action) {
		//TODO：cache it !
		if (TransactionUtils.isDefaultAction(action)) action = "default";
		ActionConfImpl ret = new ActionConfImpl();
		ret.setActionName(action);
		ret.setOwner(this);
		ret.setExpressions(getExpressions(action));
		ret.setInputFields(this.input);
		ret.setOutputFields(this.output);
		ret.setInterceptors(getInterceptors(action));
		return ret;
	}
	
	public TransactionConfImpl getExtendTransaction() {
		if (extendTransaction == null) {
			extendTransaction = this.getRelatedTransactionByName(this.extend);
		}
		return extendTransaction;
	}
	
	public TransactionConfImpl getRelatedTransactionByName(String name) {
		if (StringUtils.isEmpty(name))
			return null;
		TransactionConfImpl ret = null;
		int index = name.lastIndexOf(".");
		if (index < 0) { // 取同一个包中的交易
			ret = this.parent.getTransaction(name);
		}
		else { // 
			String packageName = name.substring(0, index);
			String transName = name.substring(index + 1);
			ret = config.getTransaction("/"+packageName.replace(".", "/"), transName);
		}
		return ret;
	}
	
	//~ getter and setter
	
	public WebConfig getConfig() {
		return config;
	}
	public String getCname() {
		return cname;
	}
	public void addExpression(ExpressionConf expression) {
		this.expressions.add(expression);
	}
	protected FieldConfListImpl getInternalInput() {
		return this.input;
	}
	protected FieldConfListImpl getInternalOutput() {
		return this.output;
	}
	public void setInput(FieldConfListImpl mc) {
		this.input = mc;
	}
	public void setOutput(FieldConfListImpl mc) {
		this.output = mc;
	}
	public void setInterceptors(List<String> interceptors) {
		this.interceptors = interceptors;
	}
	public String getName() {
		return name;
	}
	public PackageConfImpl getPackage() {
		return parent;
	}
	public String getExtend() {
		return this.extend;
	}
	public void setExtend(String extend) {
		this.extend = extend;
	}
	public List<String> getInherit() {
		return this.inherit;
	}
	public void setInherit(List<String> inherit) {
		this.inherit = inherit;
	}
	public String getParameter(String name) {
		return this.params.get(name);
	}
	public void setParameter(String name, String value) {
		this.params.put(name, value);
	}
	public List<String> getActionNames() {
		return actionNames;
	}
	public void setActionNames(List<String> actionNames) {
		this.actionNames = actionNames;
	}
	
	public String getEntityName() {
		return this.parent.getModel().getName();
	}
	
	public String getManagerName() {
		return this.parent.getModel().getManagerName();
	}

	public Class getEntityClass() {
		return this.parent.getModel().getEntityClass();
	}

	public void setType(TransactionType type) {
		this.type = type;
	}
	
	public boolean isCrudTransaction() {
		TransactionType t = this.getType();
		return t == TransactionType.create || t == TransactionType.read ||
				t == TransactionType.update || t == TransactionType.delete;
	}
	
	public TransactionType getType() {
		if (this.type == null) {
			this.type = TransactionType.resolve(this.name);
		}
		return this.type;
	}
	
	public String toString() {
		if (log.isDebugEnabled())
			return ToStringBuilder.reflectionToString(this);
		else
			return new ToStringBuilder(this).
				append("name", name).
				toString();
	}
	
	//~ action 相关

	protected List<ExpressionConf> getExpressions(String action) {
		List<ExpressionConf> ret = new ArrayList<ExpressionConf>();
		ret.addAll(this.expressions);
		if (this.isInherit(EXTEND_VALIDATOR)) {
			ret.addAll(getExtendTransaction().getExpressions(action));
		}
		return ret;
	}

	protected List<String> getInterceptors(String action) {
		if (this.interceptors == null) return interceptors;
		return Collections.unmodifiableList(interceptors);
	}
}
