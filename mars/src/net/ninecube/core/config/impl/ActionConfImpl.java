/**
 * 
 * created on 2007-1-19
 */
package net.ninecube.core.config.impl;

import java.util.List;

import net.ninecube.core.config.ActionConf;
import net.ninecube.core.config.ExpressionConf;
import net.ninecube.core.config.FieldConf;
import net.ninecube.core.config.FieldUtil;
import net.ninecube.core.config.ResultMapping;
import net.ninecube.core.config.TransactionType;
import net.ninecube.core.config.TransactionUtils;
import net.ninecube.core.config.field.FieldConfListImpl;
import net.ninecube.core.query.QueryCondition;
import net.ninecube.util.DynamicBean;
import net.ninecube.util.StringUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author jxb
 * 
 */
public class ActionConfImpl implements ActionConf {
	private static final Log log = LogFactory.getLog(ActionConfImpl.class);
	private String name;
	private TransactionConfImpl owner;
	private List<? extends ExpressionConf> expressions;
	private FieldConfListImpl inputFields;
	private FieldConfListImpl outputFields;
	private List<String> interceptors;
	private static ResultMapping mapping;
	
	static {
		try {
			mapping = (ResultMapping) Class.forName("net.ninecube.core.config.impl.DefaultResultMapping").newInstance();
		} catch (Exception e) {
			log.error("can't create ResultMapping instance.", e);
		} 
	}

	public String getName() {
		return name;
	}
	
	public String getCname() {
		return this.owner.getCname();
	}
	
	public boolean isTypeDefault() {
		return TransactionType.resolve(this.getOwner().getName()) != null;
	}

	public String getFullName() {
		return this.owner.getFullName()+"!" + this.name;
	}
	
	public boolean isDefaultAction() {
		return TransactionUtils.isDefaultAction(this.getName());
	}
	
	public ActionConf getExtendAction() {
		if (this.getOwner().isInherit(TransactionConfImpl.EXTEND_RESULT)) {
			return getOwner().getExtendTransaction().getAction(this.getName());
		}
		return null;
	}

	public TransactionConfImpl getOwner() {
		return owner;
	}

	public List<String> getInterceptors() {
		return interceptors;
	}
	
	//~ entity
	
	public Class getEntityClass() {
		return this.owner.getEntityClass();
	}
	
	public String getEntityName() {
		return this.owner.getEntityName();
	}
	
	public String getEntityManagerName() {
		return this.owner.getManagerName();
	}
	
	public String getKeyFieldName() {
		FieldConf f = this.owner.getPackage().getModel().getKey();
		if (f != null) return f.getName();
		return null;
	}
	
	//~ result

	public String getResultView(String resultcode) {
		return getResultView(resultcode, false);
	}
	
	public String getDefaultResultView(String resultcode) {
		return getResultView(resultcode, true);
	}
	
	private String getResultView(String resultcode, boolean useDefault) {
		// TODO: resultView的继承问题
		if (this.getExtendAction() != null) return getExtendAction().getResultView(resultcode);
		
		String ret = mapping.getResultView(this.owner,  this.owner.getPackage().getModel(), this, resultcode);
		// chainaction和绝对定位的视图没有默认视图
		if (ret.startsWith(">") || ret.startsWith("<") || ret.startsWith("^")) return useDefault ? null : ret;
		if (ret.startsWith("/")) return useDefault ? null : getOwner().getPackage().getViewBaseDir() + "/" + ret;
		
		if (!useDefault) {
			return getOwner().getPackage().getViewPackageName() + "/" + ret;
		}
		else {
			// TODO: 默认配置管理器 DefaultConfigManager
			if (TransactionType.query == this.getOwner().getType())
				return getOwner().getPackage().getViewBaseDir() + "/default/query";
			if (TransactionType.reflect == this.getOwner().getType())
				return getOwner().getPackage().getViewBaseDir() + "/default/query";
			if (TransactionType.create == this.getOwner().getType() ||
					TransactionType.read == this.getOwner().getType() ||
					TransactionType.update == this.getOwner().getType() ||
					TransactionType.delete == this.getOwner().getType())
				return getOwner().getPackage().getViewBaseDir() + "/default/crud";
			// 其它交易类型不支持默认视图
			return null;
		}
	}
	
	//~ validate

	public List<? extends FieldConf> getFieldValidators() {
		return ActionsFilter.filter(this.inputFields.getValidateFields(), this.name);
	}

	public List<? extends ExpressionConf> getExpressions() {
		return ActionsFilter.filter(expressions, this.name);
	}
	
	//~ input
	
	public DynamicBean newInputBean() {
		return FieldUtil.newDynamicBean(getDynamicBeanClassName(), this.getInputDataFields());
	}
	
	protected String getDynamicBeanClassName() {
		return this.owner.getTransactionClassName() + "$$" +StringUtil.capitalFirst(this.getName());
	}

	public FieldConf getInputField(String name) {
		return this.inputFields.getByName(name);
	}

	public List<? extends FieldConf> getInputStaticFields() {
		return this.inputFields.getStaticFields();
	}

	public List<? extends FieldConf> getInputViewFields() {
		return this.inputFields.getViewFields();
	}
	
	public List<? extends FieldConf> getInputDataFields() {
		return this.inputFields.getDataFields();
	}
	
	//~ output

	public FieldConf getOutputField(String name) {
		return this.outputFields.getByName(name);
	}

	public List<? extends FieldConf> getOutputViewFields() {
		return this.outputFields.getViewFields();
	}
	
	//~ query

	public List<? extends QueryCondition> getQueryFields() {
		return this.inputFields.getQueryFields();
	}
	
	//~ setter

	public void setActionName(String name) {
		this.name = name;
	}

	public void setExpressions(List<? extends ExpressionConf> expressions) {
		this.expressions = expressions;
	}

	public void setInputFields(FieldConfListImpl inputFields) {
		this.inputFields = inputFields;
	}

	public void setInterceptors(List<String> interceptors) {
		this.interceptors = interceptors;
	}

	public void setOutputFields(FieldConfListImpl outputFields) {
		this.outputFields = outputFields;
	}

	public void setOwner(TransactionConfImpl owner) {
		this.owner = owner;
	}
}
