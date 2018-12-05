package net.ninecube.venus.domain;

import net.ninecube.core.domain.EntityManager;

/**
 * 
 * @author jxb
 *
 */
public interface UserManager extends EntityManager<User>, net.ninecube.core.security.UserManager {

	public User getByName(String name);
	
}
