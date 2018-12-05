/**
 * 
 * created on 2006-12-17
 */
package net.ninecube.core.trans;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import net.ninecube.core.config.ActionConf;
import net.ninecube.core.context.RequestContext;
import net.ninecube.core.context.ServiceLocator;
import net.ninecube.core.domain.Entity;
import net.ninecube.core.domain.EntityManager;
import net.ninecube.core.security.User;
import net.ninecube.core.webwork.WebworkUtils;
import net.ninecube.util.ArrayUtil;
import net.ninecube.util.DataTypeUtil;
import net.ninecube.util.StringUtil;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionSupport;
import com.opensymphony.xwork.util.OgnlValueStack;

/**
 * 
 * @author jxb
 * 
 */
public class Transaction extends ActionSupport{

	protected static final Log log = LogFactory.getLog(Transaction.class);

	//~ action
	
	/**
	 * 返回动作
	 * TODO：系统应该记录用户的交易行为，这样便可以实现真正的“返回”动作
	 */
	public String back() {
		return SUCCESS;
	}
	
	//~ util method
	
	public Object ognl(String ognlExpression, Object obj) {
		if (StringUtil.isEmpty(obj)) return null;
		try {
			return Ognl.getValue(ognlExpression, obj);
		} catch (OgnlException e) {
			log.error("ognl error: '" + ognlExpression + "' with '" + obj + "'", e);
			return null;
		}
	}
	
	public String join(Object o, String split) {
		return ArrayUtil.join(o, split);
	}

	public Object getBean(String name) {
		return  ServiceLocator.get().getBean(name);
	}

	@SuppressWarnings("unchecked")
	public EntityManager<Entity> getEntityManager() {
		return (EntityManager<Entity>) getBean(getConfig().getEntityManagerName());
	}
	
	public String getEntityKeyName() {
		return this.getConfig().getKeyFieldName();
	}
	
	public ActionConf getConfig() {
		return WebworkUtils.getAction(ActionContext.getContext().getActionInvocation());
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getParameters()
	{
		return ActionContext.getContext().getParameters();
	}

	public String getParameter(String name)
	{
		return DataTypeUtil.obj2String(getParameters().get(name));
	}
	
	public Long getLongParameter(String name)
	{
		return DataTypeUtil.obj2Long(getParameters().get(name));
	}
	
	public String getParameter(String name, String from_ch_set, String to_ch_set) throws Exception
	{
		String val = getParameter(name);
		if (val == null) return null;
		String ret = new String(val.getBytes(from_ch_set), to_ch_set);
		log.debug("convert charset:"+val+":"+ret+":"+from_ch_set+":"+to_ch_set);
		return ret;
	}
	
	public OgnlValueStack getValueStack()
	{
		return ActionContext.getContext().getValueStack();
	}
	
	public String getActionName()
	{
		return ActionContext.getContext().getName();
	}
	
	public String numberFormat(Object num)
	{
		return numberFormat(num, "#0.00");
	}
	
	public String numberFormat(Object num, String format)
	{
		if (num instanceof Number)
			return new DecimalFormat(format).format(((Number)num).doubleValue());
		return null;
	}
	
	public String dateFormat(Object date)
	{
		return dateFormat(date, "yy-MM-dd hh:mm:ss");
	}
	
	public String dateFormat(Object date, String format)
	{
		if (date instanceof Date)
			return new SimpleDateFormat(format).format((Date)date);
		return "date format error!!";
	}
	
	public User getUser() {
		return RequestContext.get().getUser();
	}
	
	public Date getToday() {
		return new Date();
	}
	
	public String getTodayString()
	{
		return dateFormat(new Date());
	}
	
	public String getTodayString(String format)
	{		
		return dateFormat(new Date(), format);
	}
}
