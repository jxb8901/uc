/**
 * created on 2007-3-20
 */
package net.ninecube.console;

import java.util.Arrays;

import net.ninecube.formula.FormulaEngine;
import net.ninecube.lang.DateRange;
import net.ninecube.lang.FrequenceDate;
import net.ninecube.util.DateUtil;
import net.ninecube.venus.task.CalculateMetric;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

/**
 * @author JXB
 */
public class FormulaMain extends MainSupport {
	private static final Logger log = Logger.getLogger(FormulaMain.class);
	
	private static String startTargetDate = "D" + DateUtil.getYYYYMMDD();
	private static String endTargetDate = startTargetDate;

	public static void main(String[] args) throws Exception {
		new FormulaMain().execute(args);
	}

	@Override
	protected void doExecute(CommandLine commandLine) {
		if (commandLine.hasOption("c")) {
			((FormulaEngine)getBeanFactory().getBean("formulaEngine")).validateFormulas();
			return;
		}

		CalculateMetric task = new CalculateMetric();
		if (commandLine.hasOption("d")) {
			String[] d = commandLine.getOptionValues("d");
			if (d.length >= 1) {
				startTargetDate = d[0];
				endTargetDate = startTargetDate;
			}
			if (d.length >= 2) endTargetDate = d[1];
		} 
		task.setTargetDate(new DateRange(new FrequenceDate(startTargetDate), 
				new FrequenceDate(endTargetDate)));
		if (commandLine.hasOption("f")) {
			String[] formulaNames = commandLine.getOptionValues("f");
			if (formulaNames != null)
				task.setFormulaNames(Arrays.asList(formulaNames));
		}
		if (commandLine.hasOption("t")) task.setTargetType(commandLine.getOptionValue("t"));		
		if (commandLine.hasOption("r"))  task.setRecalculate(true);
		if (commandLine.hasOption("s")) task.setSaveToDB(true);
		
		task.run();
	}

	@Override
	protected Options getOptions() {
		Options options = new Options();
		options.addOption(OptionBuilder
                .withArgName("targetdate")
				.withDescription("要计算的日期范围，\n" + 
						"默认值为：" + startTargetDate + "\n" +
						"使用频度后面带日期的格式，\n" + 
						"如：Q200601表示2006年第1季度")
                .hasArgs()
				.create("d"));
		options.addOption(OptionBuilder
                .withArgName("formulaname")
                .withDescription("要计算的指标名称,可以为多值")
                .hasArgs()
                .create("f"));
		options.addOption(OptionBuilder
                .withArgName("targettype")
                .withDescription("要计算的计算对象类型")
                .hasArg()
                .create("t"));
		options.addOption(OptionBuilder
                .withArgName("saveToDB")
                .withDescription("保存结果到数据库，默认保存结果到result.log文件中")
                .hasArg(false)
                .create("s"));
		options.addOption(OptionBuilder
                .withArgName("check")
                .withDescription("检查公式定义是否正确")
                .hasArg(false)
                .create("c"));
		options.addOption(OptionBuilder
                .withArgName("recalculate")
                .withDescription("重新计算数据")
                .hasArg(false)
                .create("r"));
		return options;
	}
}
