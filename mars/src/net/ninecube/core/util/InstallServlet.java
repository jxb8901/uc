/*
 * Created on 2004-5-28
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.ninecube.core.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ninecube.core.context.ServiceLocator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

/**
 * @author jxb
 */
public class InstallServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(InstallServlet.class);

	private InstallService installService;

	/**
	 * 
	 */
	public void init(ServletConfig config) throws ServletException {
		log.info("start install ...");
		installService = (InstallService) ServiceLocator.get().getBean("installService");
		install(true); //@ TODO: 开发模式才允许强制安装

		log.info("... end install");
	}

	/**
	 * 
	 */
	public void doService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("Attempt to call service method on InstallServlet as " + request.getRequestURI() + " was ignored");
		}
		response.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	public String getServletInfo() {
		return "InstallServlet";
	}
	
	protected void install(boolean focusInstall) {
		if (!focusInstall && installService.hasInstalled()) {
			System.out.println("系统已经初始化! 强制初始化会导致数据全部丢失!" + "\r\n 使用-f参数可强制初始化系统.");
		} else {
			createTables();
			installService.install();
			log.info("...系统初始化完成");
		}
	}

	protected void createTables() {
		log.info("开始创建数据库表...");
		LocalSessionFactoryBean sessionFactory = (LocalSessionFactoryBean) ServiceLocator.get().getBean("&sessionFactory");
		sessionFactory.dropDatabaseSchema();
		sessionFactory.createDatabaseSchema();
		log.info("...建表完成");
	}

	public static void main(String[] arg) throws Exception {
		boolean focusInstall = false;
		List<String> xmls = new ArrayList<String>();
		for (int i = 0; i < arg.length; i++) {
			if (arg[i].endsWith(".xml"))
				xmls.add(arg[i]);
			if ("-f".equals(arg[i]))
				focusInstall = true;
		}

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext((String[]) xmls.toArray(new String[xmls.size()]));
		ServiceLocator.get().setBeanFactory(ctx);
		InstallService is = (InstallService) ctx.getBean("installService");
		InstallServlet in = new InstallServlet();
		in.installService = is;
		in.install(focusInstall);
	}
}
