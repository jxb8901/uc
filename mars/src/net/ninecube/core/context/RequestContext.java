/**
 * 2005-8-14
 */
package net.ninecube.core.context;

import java.util.Map;

import net.ninecube.core.security.PermissionChecker;
import net.ninecube.core.security.User;


/**
 * 有关当前请求的上下文相关信息，包括当前用户、IP地址等其它信息
 * @author JXB
 *
 */
public class RequestContext {
	public static final String KEY_CURRENT_USER = "net.ninecube.current.user";
	private PermissionChecker permissionChecker;
	
	private static ThreadLocal context = new ThreadLocal() {
		 protected synchronized Object initialValue() {
			return new RequestContext();
		}
	};
	
	public static RequestContext get() {
		return (RequestContext) context.get();
	}
	
	private Map<String, Object> session;
	//当前用户IP
	private String ip;
	//当前操作的用户
	private User user;
	
	public void release() {
		this.ip = null;
		this.user = null;
		this.session = null;
	}
	
	//~ getter and setter

	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Map<String, Object> getSession() {
		return session;
	}
	
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public PermissionChecker getPermissionChecker() {
		return permissionChecker;
	}

	public void setPermissionChecker(PermissionChecker permissionChecker) {
		this.permissionChecker = permissionChecker;
	}
}
