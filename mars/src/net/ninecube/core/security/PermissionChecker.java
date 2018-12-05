package net.ninecube.core.security;

import java.util.List;

import net.ninecube.core.exception.NoAuthenticationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PermissionChecker {
	private static Log log = LogFactory.getLog(PermissionChecker.class);

	private List<String> publicPerms;
	private List<String> commUserPerms;

	public boolean isGrant(User user, String permission){
		log.debug("check permission : " + permission + " ; current user : " + user);
		
		if(this.publicPerms.contains(permission)) return true;
		if(user == null ) throw new NoAuthenticationException("用户还没登陆,试图访问受保护资源'" + permission + "'");
		if(this.commUserPerms.contains(permission)) return true;
		return user.isGranted(permission);
	}
	
	public void setPublicPermissions(List<String> publicPerms){
		this.publicPerms = publicPerms;
	}
	
	public void setCommonUserPermissions(List<String> commUserPerms){
		this.commUserPerms = commUserPerms;
	}
	
}
