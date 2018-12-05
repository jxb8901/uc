/**
 * created on 2006-10-22
 */
package net.ninecube.core.trans.system;

import net.ninecube.core.context.RequestContext;
import net.ninecube.core.security.User;
import net.ninecube.core.security.UserManager;
import net.ninecube.core.trans.Transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author  jxb
 */
public class Login extends Transaction {
	private static final Log LOG = LogFactory.getLog(Login.class);
	private String userid;
	private String password;
	private UserManager userManager;

	public String execute() throws Exception {
		LOG.debug("login in default action, userid='"+userid+"', password='"+password+"'");
		return super.SUCCESS;
	}
	
	public String submit() throws Exception {
		LOG.debug("login in submit action, userid='"+userid+"', password='"+password+"'");
		User curUser = this.userManager.getByName(userid);
		if(curUser == null || !curUser.checkPassword(password)){
			super.addActionError("不存在的用户名和密码！");
			return INPUT;
		}
		RequestContext.get().setUser(curUser);
		RequestContext.get().getSession().put(RequestContext.KEY_CURRENT_USER, curUser);
		return SUCCESS;
	}
	
	public String logout(){
		RequestContext.get().getSession().put(RequestContext.KEY_CURRENT_USER, null);
		return INPUT;
	}

	/**
	 * @param loginId
	 * @uml.property  name="loginId"
	 */
	public void setUserid(String loginId) {
		this.userid = loginId;
	}

	/**
	 * @param password
	 * @uml.property  name="password"
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public String getUserid() {
		return userid;
	}
	
	public void setUserManager(UserManager userManager){
		this.userManager = userManager;
	}
}
