package net.ninecube.core.security;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface User {

	public List<? extends Role> getRoles();
	
	public boolean isGranted(String perm);
	
	public boolean checkPassword(String password);
}
