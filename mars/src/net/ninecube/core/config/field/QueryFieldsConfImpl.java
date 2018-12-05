/**
 * 
 * created on 2007-1-13
 */
package net.ninecube.core.config.field;

import java.util.ArrayList;
import java.util.List;

import net.ninecube.core.config.ConfigException;
import net.ninecube.core.config.FieldConf;
import net.ninecube.core.config.FieldSource;
import net.ninecube.core.config.impl.FieldConfImpl;
import net.ninecube.core.query.Operator;

/**
 * 
 * @author jxb
 * 
 */
public class QueryFieldsConfImpl extends FieldConfImpl implements ReferenceFieldsConf {
	private Operator operator;
	
	public List<FieldConf> resolve(FieldSource model) {
		List<FieldConf> ret = new ArrayList<FieldConf>();
		FieldConf f = model.resolveReference(this.getName());
		// 如果f是模型字段，则应该报错，对于模型字段的引用作为查询条件应该在name中使用model.name的形式
		if (f instanceof ModelFieldsConf) throw new ConfigException("can't reference model field in queryfields");
		// 应该可以考虑将operator作为field的一般属性，那样就不需要每个字段单独定义
		// 对于查询交易，需要将其dataName作变换，以与name区别
		if (Operator.between.equals(this.operator)) {
			ret.add(copyField(f, "Start", "起始", Operator.ge));
			ret.add(copyField(f, "End", "结束", Operator.le));
		}
		else {
			ret.add(copyField(f, "Q", "", this.operator));
		}
		return ret;
	}
	
	private FieldConf copyField(FieldConf f, String nameSuffix, String cnamePrefix, Operator op) {
		// 注意：这里的复制操作支持模型引用字段（即形如customer.id这样的字段）
		// 此时返回的字段的name属性为"idQ"，entityName属性为"customer.id"这样正好可以作为hibernate查询使用
		// TODO: 要注意的是这里可能有一个潜在的错误，即如下配置导致客户端出现两个同名控件
		// <queryfield name="customer.name" op="like" />
		// <queryfield name="name" op="like" />
		if (!(f instanceof FieldConfImpl))
			throw new IllegalArgumentException("must be type'" + FieldConfImpl.class.getName() + "'");
		// TODO: 能否不用强制转型的方式实现merge及setXxxx方法？
		FieldConfImpl nf = ((FieldConfImpl)f).merge(this);
		nf.setName(f.getName()+nameSuffix);
		nf.setCname(cnamePrefix + f.getCname());
		nf.setEntityName(this.getName());
		nf.setOperator(op);
		nf.setValue(this.getValue());
		return nf;
	}
	
	//~ getter and setter 

	public Operator getOperator() {
		return this.operator;
	}
	
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
}
