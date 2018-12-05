/**
 * 
 * created on 2007-6-12
 */
package net.ninecube.venus.trans.task.definition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.ninecube.core.config.ActionConf;
import net.ninecube.core.config.FieldConf;
import net.ninecube.core.config.FieldTagType;
import net.ninecube.core.config.FieldUtil;
import net.ninecube.core.config.field.FieldConfListImpl;
import net.ninecube.core.config.impl.ActionConfImpl;
import net.ninecube.core.config.impl.FieldConfImpl;
import net.ninecube.core.config.impl.TransactionConfImpl;
import net.ninecube.core.task.TaskDefinition;
import net.ninecube.core.task.TaskEngine;
import net.ninecube.core.trans.Transaction;
import net.ninecube.core.webwork.WebworkUtils;
import net.ninecube.util.DynamicBean;

import com.opensymphony.xwork.ModelDriven;
import com.opensymphony.xwork.Preparable;

/**
 * 
 * @author jxb
 */
public class Run extends Transaction
		implements ModelDriven, Preparable {
	private TaskEngine taskDefinitionManager;
	private TaskDefinition taskDefinition;
	private ActionConf actionConf;
	private DynamicBean model;
	
	public String submit() {
		this.taskDefinitionManager.executeImmediately(
				this.taskDefinition.getId(), this.model.toMap());
		return SUCCESS;
	}

	@Override
	public ActionConf getConfig() {
		return this.actionConf;
	}

	public Object getModel() {
		return model;
	}

	public void prepare() throws Exception {
		this.taskDefinition = this.taskDefinitionManager.getTask(super.getParameter("idno"));
		this.actionConf = createActionConf(super.getActionName() + "_" + this.taskDefinition.getId(), 
				this.taskDefinition.getParameters());
		this.model = this.actionConf.newInputBean();
	}

	public void setTaskEngine(TaskEngine taskDefinitionManager) {
		this.taskDefinitionManager = taskDefinitionManager;
	}

	private static ActionConf createActionConf(String action, FieldConf[] fields) {
		TransactionConfImpl trans = (TransactionConfImpl)WebworkUtils.getTransaction();
		ActionConfImpl ret = new ActionConfImpl();
		ret.setActionName(action);
		ret.setOwner(trans);
		FieldConfListImpl input = new FieldConfListImpl();
		FieldConfImpl key = (FieldConfImpl)trans.getPackage().getModel().getKey();
		key = key.clone();
		key.setTagtype(FieldTagType.hidden);
		input.add(key);
		for (FieldConf field : fields) input.add((FieldConfImpl)field);
		input.resolve(trans.getPackage().getModel(), true);
		ret.setInputFields(input);
		return ret;
	}
}
