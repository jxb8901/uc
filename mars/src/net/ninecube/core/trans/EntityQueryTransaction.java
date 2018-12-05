/**
 * created on 2007-1-12
 */
package net.ninecube.core.trans;

import net.ninecube.core.config.ActionConf;
import net.ninecube.core.config.QueryObjectFactory;
import net.ninecube.core.query.QueryObject;
import net.ninecube.util.DynamicBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork.ModelDriven;
import com.opensymphony.xwork.Preparable;

/**
 * @author  jxb
 */
public class EntityQueryTransaction extends PageTransaction implements Preparable, ModelDriven {
	private static final Log log = LogFactory.getLog(EntityQueryTransaction.class);
	private transient QueryObjectFactory queryFactory;
	private DynamicBean parameters;

	@Override
	public String execute() {
		log.debug("begin query transaction ...");
		ActionConf config = getConfig();
		log.debug("query transaction: '"+config.getFullName()+"'");
		
		QueryObject queryObject = queryFactory.getQueryObject(config);
		this.setPage(queryObject.find(parameters.toMap(), getCurrentPage(), getPageSize()));
		
		log.debug("... end query transaction");
		return SUCCESS;
	}

	public void setQueryObjectFactory(QueryObjectFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	public DynamicBean getModel() {
		return parameters;
	}

	public void prepare() throws Exception {
		this.parameters = super.getConfig().newInputBean();
	}
}
