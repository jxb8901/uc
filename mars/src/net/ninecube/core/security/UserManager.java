package net.ninecube.core.security;

import net.ninecube.core.domain.EntityManager;

/**
 * 
 * @author jxb
 *
 */
public interface UserManager {
	
	public User getByName(String name);

}
