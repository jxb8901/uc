/**
 * 
 * created on 2007-4-25
 */
package net.ninecube.venus.domain.impl;

import net.ninecube.core.domain.AbstractEntityManager;
import net.ninecube.core.exception.EntityIsUsingException;
import net.ninecube.venus.domain.PointType;
import net.ninecube.venus.domain.PointTypeManager;

/**
 * 
 * @author jxb
 */
public class PointTypeManagerImpl extends AbstractEntityManager<PointType> implements PointTypeManager {

	@Override
	public PointType onDelete(PointType cg) {
		if (!isDeletable(cg)) throw new EntityIsUsingException(cg);
		return cg;
	}

	private boolean isDeletable(PointType cg) {
		// TODO: 检查积分类型是否已使用
		return true;
	}

}
