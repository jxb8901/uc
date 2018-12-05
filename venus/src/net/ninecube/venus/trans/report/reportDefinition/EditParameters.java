package net.ninecube.venus.trans.report.reportDefinition;

import java.util.List;

import net.ninecube.core.domain.EntityManager;
import net.ninecube.core.domain.GenericEntityManager;
import net.ninecube.core.query.Page;
import net.ninecube.core.trans.EntityQueryTransaction;
import net.ninecube.reports.objects.Report;

public class EditParameters extends EntityQueryTransaction {
	private Integer idno;
	private EntityManager entityManager;
	
	public String execute(){
		List paras = ((Report)entityManager.getById(new Integer("" + getModel().get("idno")))).getParameters();
		
		this.setPage(new Page(paras, paras.size(), getCurrentPage(), getPageSize()));
		
		
		return "SUCCESS";
	}

	public Integer getIdno() {
		return idno;
	}

	public void setIdno(Integer idno) {
		this.idno = idno;
	}
	
	public void setReportDefinitionManager(EntityManager entityManager){
		this.entityManager = entityManager;
	}

}
