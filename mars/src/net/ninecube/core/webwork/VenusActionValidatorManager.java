/**
 * 
 * created on 2006-12-17
 */
package net.ninecube.core.webwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.ninecube.core.config.ActionConf;
import net.ninecube.core.config.ExpressionConf;
import net.ninecube.core.config.FieldConf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.validator.ActionValidatorManagerFactory;
import com.opensymphony.xwork.validator.DefaultActionValidatorManager;
import com.opensymphony.xwork.validator.Validator;
import com.opensymphony.xwork.validator.ValidatorConfig;
import com.opensymphony.xwork.validator.ValidatorFactory;

/**
 * 
 * @author jxb
 * 
 */
public class VenusActionValidatorManager extends DefaultActionValidatorManager {
	private static final Map<String, List<ValidatorConfig>> validatorCache = Collections.synchronizedMap(new HashMap<String, List<ValidatorConfig>>());
	private static final Log log = LogFactory.getLog(VenusActionValidatorManager.class);
	private static final String DEFAULT = "default";
	private ValidatorAdaptor validatorAdaptor;
	
	public VenusActionValidatorManager(ValidatorAdaptor validatorAdaptor) {
		this.validatorAdaptor = validatorAdaptor;
	}
	
	public void init() {
		log.debug("初始化自定义验证管理器");
		ActionValidatorManagerFactory.setInstance(this);
	}

	protected void clearValidatorCache() {
		validatorCache.clear();
	}

	@Override
	public synchronized List getValidators(Class clazz, String context) {
		String method = ActionContext.getContext().getActionInvocation().getProxy().getMethod();
		ActionConf tc = WebworkUtils.getActionIfExists();
		// 如果找不到交易配置，则认为是普通的webwork交易
		if (tc == null) {
			tc = WebworkUtils.getActionByClassAndAction(clazz, context);
			if (tc == null) {
				log.info("找不到交易配置，使用WebWork默认验证机制:"+clazz.getName()+","+context);
				return super.getValidators(clazz, context);
			}
		}

		List<ValidatorConfig> cfgs = getValidators(tc);

		List<Validator> validators = new ArrayList<Validator>(cfgs.size());
		// create clean instances of the validators for the caller's use
		for (Iterator iterator = cfgs.iterator(); iterator.hasNext();) {
			ValidatorConfig cfg = (ValidatorConfig) iterator.next();
			Validator validator = ValidatorFactory.getValidator(cfg);
			validator.setValidatorType(cfg.getType());
			validators.add(validator);
		}

		return validators;
	}

	protected List<ValidatorConfig> getValidators(ActionConf tc) {
		String key = tc.getFullName();
		List<ValidatorConfig> ret = validatorCache.get(key);
		if (ret == null) {
			ret = new ArrayList<ValidatorConfig>();
			for (FieldConf fc : tc.getFieldValidators()) {
				ret.addAll(validatorAdaptor.adapt(fc));
			}
			for (ExpressionConf ec : tc.getExpressions()) {
				ret.add(validatorAdaptor.adapt(ec));
			}
			validatorCache.put(key, ret);
		}
		return ret;
	}
}
