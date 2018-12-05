package net.ninecube.reports.objects;

import net.ninecube.core.domain.Entity;

public abstract class ReportEntity implements Entity{
	
	public abstract void setId(Integer id);
	public abstract Integer getId();
	
	public Integer getIdno(){
		return getId();
	}
	
	public void setIdno(Integer id){
		setId(id);
	}
	
}
