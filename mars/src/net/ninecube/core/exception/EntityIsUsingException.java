/**
 * 
 * created on 2007-2-1
 */
package net.ninecube.core.exception;

import net.ninecube.core.domain.Entity;


/**
 * 对象正在被使用，不能删除
 * @author jxb
 * 
 */
public class EntityIsUsingException extends EntityException {
	private static final String MSG = "对象正在被使用，不能删除";

	public EntityIsUsingException(Entity entity, String message, Throwable cause) {
		super(message, cause);
		super.setEntity(entity);
	}

	public EntityIsUsingException(Entity entity, String message) {
		super(message);
		super.setEntity(entity);
	}

	public EntityIsUsingException(Entity entity, Throwable cause) {
		super(MSG, cause);
		super.setEntity(entity);
	}

	public EntityIsUsingException(Entity entity) {
		super(MSG);
		super.setEntity(entity);
	}

}
