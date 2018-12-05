/**
 * 
 */
package net.ninecube.core.config.field;

import java.util.Map;

import net.ninecube.core.config.ConfigException;
import net.ninecube.core.config.FieldType;
import net.ninecube.core.config.impl.FieldConfImpl;
import net.ninecube.util.EnumerManager;
import net.ninecube.util.StringUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author mike
 *
 */
public class EnumFieldConf extends FieldConfImpl {
	private static final Log log = LogFactory.getLog(EnumFieldConf.class);
	// TODO: 修改为和DTD中的名称一致
	private String enumname;
	private boolean multi = false;
	private FieldType elementFieldType;
	private String elementtype;

	public EnumFieldConf(){
		/* 注意这里要显示调用父类的方法 */
		super.setType(FieldType.enumt);
	}
	
	@Override
	public void setType(FieldType type) {
		this.elementFieldType = type;
	}
	
	public String getEnumname() {
		if (StringUtil.isEmpty(this.enumname) && this.elementFieldType == FieldType.bool)
			this.enumname = "yesno"; // TODO: 对布尔值的枚举类型的内建支持
		return enumname;
	}

	public Map<Object, String> getEnum(){
		return EnumerManager.get().getValues(this.getEnumname());
	}

	public boolean isMulti() {
		return multi;
	}

	public String getElementtype() {
		return elementtype;
	}

	public void setElementtype(String elementtype) {
		this.elementtype = elementtype;
	}
	
	public Class getElementClass(){
		if (StringUtil.isEmpty(this.getElementtype())) return null;
		try{
			return Class.forName(getElementtype());
		}catch(Exception e){
			throw new ConfigException("Illegal element type parameter : " + getElementtype(), e);
		}
	}

	public void setEnumname(String enumName) {
		this.enumname = enumName;
	}

	public void setMulti(boolean multi) {
		this.multi = multi;
	}

}
