/*
 * Created on 2004-3-11
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.ninecube.core.trans;

import net.ninecube.core.query.Page;

/**
 * @author jxb
 */
public class PageTransaction extends Transaction {
	private static final int PAGESIZE = 10;

	private int currentPage;
	private int pageSize = PAGESIZE;
	private Page page = Page.EMPTY_PAGE;

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		if (currentPage <= 0)
			currentPage = 1;
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
}
