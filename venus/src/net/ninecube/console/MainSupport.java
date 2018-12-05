/**
 * created on 2007-3-20
 */
package net.ninecube.console;

import javax.sql.DataSource;

import net.ninecube.core.context.ServiceLocator;
import net.ninecube.core.util.OpenSessionInViewFilterSimulator;
import net.ninecube.db.DBManager;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author JXB
 */
public abstract class MainSupport {	
	private BeanFactory beanFactory;

	public void execute(String[] args) throws Exception {
		Options options = getOptions();
		options.addOption("h", false, "显示本帮助信息");

		CommandLineParser parser = new BasicParser();
		CommandLine cl = parser.parse(options, args);
		if (cl.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("run", options, true);
			return;
		}

		beanFactory = new ClassPathXmlApplicationContext(new String[] { 
				"applicationContext-beans.xml",
				"applicationContext-hibernate.xml",
				"applicationContext-transaction.xml",
				"applicationContext-mysql.xml",
				"applicationContext-formula.xml",
				"applicationContext-rulebeans.xml",
				});
		ServiceLocator.get().setBeanFactory(beanFactory);

		doExecute(cl);
	}
	
	protected BeanFactory getBeanFactory() {
		return this.beanFactory;
	}
	
	protected abstract Options getOptions();
	
	protected abstract void doExecute(CommandLine commandLine);
}
