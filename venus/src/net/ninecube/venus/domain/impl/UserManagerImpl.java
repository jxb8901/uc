package net.ninecube.venus.domain.impl;

import net.ninecube.core.domain.AbstractEntityManager;
import net.ninecube.venus.domain.User;
import net.ninecube.venus.domain.UserManager;

public class UserManagerImpl extends AbstractEntityManager<User> implements UserManager {
	
	public User getByName(String name){
		return super.getByNamedQuery("getUserByName", "name", name);
	}
	
}
