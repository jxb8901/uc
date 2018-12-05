/**
 * created on 2007-1-12
 */
package net.ninecube.core.trans;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.ninecube.core.config.ConfigException;
import net.ninecube.core.config.FieldConf;
import net.ninecube.core.context.ServiceLocator;
import net.ninecube.util.DynamicBean;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork.ModelDriven;
import com.opensymphony.xwork.Preparable;

/**
 * @author  jxb
 */
public class ReflectTransaction extends Transaction implements Preparable, ModelDriven {
	private static final Log log = LogFactory.getLog(ReflectTransaction.class);
	private DynamicBean parameters;
	private Object result;
	
	@Override
	public String execute() throws Exception {
		Object manager = this.getManager();
		String method = this.getConfig().getOwner().getName();
		
		List<? extends FieldConf> fields = this.getConfig().getInputDataFields();
		List<Class> fieldTypes = new ArrayList<Class>();
		List<Object> fieldValues = new ArrayList<Object>();
		for (FieldConf f : fields) {
			fieldTypes.add(f.getType().toClass());
			fieldValues.add(this.parameters.get(f.getName()));
		}
		
		try {
			this.result = MethodUtils.invokeMethod(manager, method, 
					fields.isEmpty() ? null : fieldValues.toArray(), 
					fields.isEmpty() ? null : fieldTypes.toArray(new Class[0]));
		} catch (NoSuchMethodException e) {
			throw new ConfigException("no such method '"+method + 
					"' in class '" + manager.getClass() + "'");
		} catch (InvocationTargetException e) {
			Throwable targetException = e.getTargetException();
			if (targetException instanceof Exception) {
				throw (Exception) targetException;
			}
			throw e;
		}
		return SUCCESS;
	}

	public Object getModel() {
		return parameters;
	}

	public void prepare() throws Exception {
		this.parameters = this.getConfig().newInputBean();
	}
	
	protected Object getManager() {
		return this.getBean(getConfig().getEntityManagerName());
	}

	public Object getResult() {
		return result;
	}
}
