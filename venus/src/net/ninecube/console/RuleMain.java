/**
 * created on 2007-3-20
 */
package net.ninecube.console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import net.ninecube.db.DBManager;
import net.ninecube.formula.Formula;
import net.ninecube.formula.FormulaEngine;
import net.ninecube.formula.FormulaResolver;
import net.ninecube.formula.TargetManager;
import net.ninecube.lang.DateRange;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.util.DateUtil;
import net.ninecube.util.StringUtil;
import net.ninecube.venus.task.CalculateMetric;
import net.ninecube.venus.task.CalculatePromotionPlan;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author JXB
 */
public class RuleMain extends MainSupport {
	private static final Logger log = Logger.getLogger(RuleMain.class);
	
	private static String targetDate = "" + DateUtil.getYYYYMMDD();

	public static void main(String[] args) throws Exception {
		new RuleMain().execute(args);
	}

	@Override
	protected void doExecute(CommandLine commandLine) {
		CalculatePromotionPlan task = new CalculatePromotionPlan();
		if (!commandLine.hasOption("d")) {
			log.error("Must specify a date range for run !!!");
			return;
		}
		String[] ops = commandLine.getOptionValues("d");
		if(ops.length < 1){
			log.error("Must specify a date range for run !!!");
			return;
		}
		Date startDate = DateUtil.parseDate(ops[0],"yyyyMMdd");
		Date endDate = startDate;
		if(ops.length > 1)
			endDate = DateUtil.parseDate(ops[1],"yyyyMMdd");
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		for(;c.getTime().compareTo(endDate) <= 0;c.add(Calendar.DAY_OF_MONTH, 1)){
			log.debug("calculate day : " + DateUtil.getYYYYMMDD(c.getTime()));
			task.setTargetDate(c.getTime());		
			task.run();
		}
	}

	@Override
	protected Options getOptions() {
		Options options = new Options();
		options.addOption(OptionBuilder
                .withArgName("targetdates")
				.withDescription("要计算的日期，\n" + 
						"默认值为：" + targetDate + "\n" +
						"格式: yyyy-MM-dd" )
		                .hasArgs()
					.create("d"));
		return options;
	}
}
