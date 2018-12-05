/**
 * 
 * created on 2007-3-13
 */
package net.ninecube.core.webwork.util;

import java.util.List;

import net.ninecube.core.config.ActionConf;
import net.ninecube.core.config.FieldConf;
import net.ninecube.core.config.ModelConf;
import net.ninecube.core.config.field.EnumFieldConf;
import net.ninecube.core.config.field.ModelFieldsConf;
import net.ninecube.core.webwork.WebworkUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

import com.opensymphony.xwork.util.DefaultObjectTypeDeterminer;
import com.opensymphony.xwork.util.ObjectTypeDeterminerFactory;
import com.opensymphony.xwork.util.XWorkConverter;

/**
 * 
 * @author jxb
 * TODO : 查找当前FieldConf时,是在getInputViewFields()还是在getOutputViewFields()中查找,
 * 		当前是在ModelConf中查找
 */
public class VenusObjectTypeDeterminer extends DefaultObjectTypeDeterminer {
	private static Log log = LogFactory.getLog(VenusObjectTypeDeterminer.class);
	private Object sf;

	@Override
	public Class getElementClass(Class parentClass, String property, Object key) {
		ActionConf af = WebworkUtils.getActionIfExists();
		if (af != null) {
			ModelConf mc = af.getOwner().getPackage().getModel();
			Class rs = null;
			log.debug("*** class : " + parentClass + " ; property : " + property + "mc : " + mc);
			if(mc != null){
				List<? extends FieldConf> fs = mc.getFields();
				// TODO: ModelConf中增加getField(String name)的方法
				for(FieldConf fc : fs){
					if (fc.getName().equals(property)) {
						if(fc instanceof EnumFieldConf){
							log.debug("enumField : " + fc +" ; element type : " + ((EnumFieldConf)fc).getElementtype());
							rs = ((EnumFieldConf)fc).getElementClass();
						}
						else if (fc instanceof ModelFieldsConf) {
							if (((ModelFieldsConf)fc).getModel() instanceof ModelConf) {
								rs = ((ModelConf)((ModelFieldsConf)fc).getModel()).getEntityClass();
							}
						}
						break;
					}
				}
			}
			if(rs != null) {
				log.debug("determined class : " + rs);
				return rs;
			}
		}
		// return super.getElementClass(parentClass, property, key);
		return String.class;
	}

	@Override
	public Class getKeyClass(Class parentClass, String property) {
		return String.class;
	}

	public void init() {
		ObjectTypeDeterminerFactory.setInstance(this);
		XWorkConverter.getInstance().setObjectTypeDeterminer(this);
	}
	
	public void setSessionFactory(SessionFactory sf){
		this.sf = sf;
	}
}
