/**
 * 
 * created on 2007-2-1
 */
package net.ninecube.core.exception;

import net.ninecube.core.domain.Entity;
import net.ninecube.lang.BaseException;

/**
 * 实体对象异常
 * @author jxb
 * 
 */
public class EntityException extends BaseException {
	private Entity entity;
	
	public EntityException() {
		super();
	}

	public EntityException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityException(String message) {
		super(message);
	}

	public EntityException(Throwable cause) {
		super(cause);
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

}
