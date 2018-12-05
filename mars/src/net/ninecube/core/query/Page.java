
package net.ninecube.core.query;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 数据页对象, 用于实现数据的分页访问
 * @auth jxb
 */
public class Page implements Serializable
{
	private static final Log log = LogFactory.getLog(Page.class);
	public static final Page EMPTY_PAGE = new Page(Collections.EMPTY_LIST, 0, 1, 1);

    /**
     * 总记录数
     */
    private int recordCount = 0;

    /**
     * 每页记录数
     */
    private int size = 1;

    /**
     * 当前页号
     */
    private int current = 1;

    /**
     * 记录列表
     */
    private List records = Collections.EMPTY_LIST;

    /**
     * 构造器
     * @param pageNum 页号
     * @param pageSize 每页记录数
     */
    public Page(int currentPage, int pageSize)
    {
        this.current = currentPage < 1 ? 1 : currentPage;
        this.size = pageSize < 1 ? 1 : pageSize;
    }

	/**
	 * 构造器
	 * @param records 记录集
	 * @param count 总记录数
	 * @param currentPage 页号
	 * @param pageSize 每页记录数
	 */
	public Page(List records, int count, int currentPage, int pageSize)
	{
		this(currentPage, pageSize);
		this.records = records;
		this.recordCount = count;
	}

    /**
     * 取总记录数
     * @return int
     */
    public int getRecordCount()
    {
        return recordCount;
    }

    /**
     * 取总页数
     * @return int
     */
    public int getCount()
    {
        int i = recordCount % size;
        int j = recordCount / size;

        return (i == 0) ? j : j + 1;
    }

    /**
     * 取每页记录数
     * @return int
     */
    public int getSize()
    {
        return size;
    }

    /**
     * 取本页页号, 页号从1开始编号
     * @return int
     */
    public int getCurrent()
    {
        return current;
    }

    /**
     * 取开始记录索引号,从1开始计数
     * @return
     */
    public int getStartIndex()
    {
    	return (getCurrent()-1)*getSize()+1;
    }

    /**
     * 取开始记录索引号,从0开始计数
     * @return
     */
    public int getStartIndexFrom0()
    {
    	return getStartIndex() - 1;
    }

    /**
     * 取页中的记录列表
     * @return List
     */
    public List getRecords()
    {
        return records;
    }
    
    //~ for query object

    public void setRecords(List list, int recordCount)
    {
    	this.records = list;
    	this.recordCount = recordCount;
    }

    public String toString()
    {
        return "[recordCount="+this.recordCount+"|pageSize="+
            this.size+"|pageNum="+this.current+"]";
    }
}
