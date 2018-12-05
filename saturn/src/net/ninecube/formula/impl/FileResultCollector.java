/**
 * created on 2006-4-10
 */
package net.ninecube.formula.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import net.ninecube.formula.Context;
import net.ninecube.formula.Result;
import net.ninecube.util.DateUtil;

import org.apache.log4j.Logger;

/**
 * @author JXB
 */
public class FileResultCollector extends CacheResultCollector {
	private static final Logger log = Logger.getLogger(FileResultCollector.class);

	private String resultFileName = "formula.result.txt";
	private boolean firstSave = true;
	
	@Override
	public boolean isAccepted(Context context, Result result) {
		if (result == null) return false;
		return !context.getBoolean(Context.CONFIG_SAVETODB) && result.getValue() != null;
	}
	
	protected void saveResult(Context context, Result result) {
		log.debug("保存公式计算结果到文件："+context.getFormula().getName()+"["+getResultKey(context)+","+result.getValue()+"]");
		try {
			FileWriter writer = new FileWriter(resultFileName, true);
			if (firstSave) {
				writer.write("-- " + DateUtil.getYYYYMMDD() + " " + DateUtil.getHHMMSS());
				writer.write("\r\n");
				firstSave = false;
			}
			writer.write(getResultKey(context));
			writer.write(",");
			DecimalFormat f = new DecimalFormat("0.00");
			writer.write(f.format(result.getValue()));
			writer.write("\r\n");
			writer.close();
		} catch (IOException e) {
			log.warn("保存出错", e);
		}
	}
}
