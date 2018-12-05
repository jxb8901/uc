/**
 * 
 */
package net.ninecube.saturn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ninecube.lang.FrequenceDate;
import net.ninecube.saturn.database.DataSet;
import net.ninecube.saturn.database.DatabaseManager;
import net.ninecube.saturn.exception.ExceptionProcess;
import net.ninecube.saturn.exception.ExceptionProcessFactory;
import net.ninecube.saturn.sql.SQLExecutor;

/**
 * @author Fox
 * 
 */
public class Context {
	private DataSet dataset;

	private Map<String, Object> attributes = new HashMap<String, Object>();

	private Map<String, ?> vars = new HashMap<String, String>();

	private List<FrequenceDate> freqDates;
	
	private FrequenceDate currentFrequence;

	private DatabaseManager dbmng;

	private SQLExecutor sqlExecutor;
	
	private ExceptionProcess exceptionProcess = ExceptionProcessFactory.getExceptionProcess(ExceptionProcessFactory.ProcessType.RETHROW);
	
	private List<ContextListener> listeners = new ArrayList<ContextListener>();
	
	private NameResolver nameResolver = NameResolverProvider.getDefaultChainResolver();
	
	private boolean inline = false;

	public Context(DatabaseManager dbmng) {
		this.dbmng = dbmng;
		dataset = dbmng.newDataSet();
	}
	
	public void addListener(ContextListener listener){
		listeners.add(listener);
	}
	
	public boolean containsListener(ContextListener listener){
		return listeners.contains(listener);
	}
	
	public void finish(){
		for(ContextListener listener : listeners){
			listener.onFinished(this);
		}
	}

	public void init() {
		for(ContextListener listener : listeners){
			listener.onInit(this);
		}
	}

	public ContextListener getListenerByName(String name) {
		for(ContextListener listener : listeners){
			if (listener.getName().equals(name)) return listener;
		}
		return null;
	}
	
	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	public void setAttribute(String name, Object val) {
		attributes.put(name, val);
	}

	public DataSet getDataSet() {
		return dataset;
	}

	public DataSet setDataSet(DataSet rel) {
		dataset = rel;
		return dataset;
	}

	public Map<String, ?> getVariables() {
		return vars;
	}

	public void setVariables(Map<String, ?> binding) {
		vars = binding;
	}

	public List<FrequenceDate> getFrequenceDates() {
		return freqDates;
	}

	public void setFrequenceDates(List<FrequenceDate> freqDates) {
		this.freqDates = freqDates;

	}

	public DatabaseManager getDatabaseManager() {
		return dbmng;
	}

	public SQLExecutor getSqlExecutor() {
		return this.sqlExecutor;
	}

	public void setSqlExecutor(SQLExecutor sqlExecutor) {
		this.sqlExecutor = sqlExecutor;
		
	}
	
	/**
	 * 确定当前Context是否是Inline Context,即是否是在别一个Context执行期内
	 * @return
	 */
	public boolean isInline() {
		return inline;
	}

	public void setInline(boolean inline) {
		this.inline = inline;
		
	}

	public FrequenceDate getCurrentFrequence() {
		return currentFrequence;
	}

	public void setCurrentFrequence(FrequenceDate currentFrequence) {
		this.currentFrequence = currentFrequence;
	}

	public ExceptionProcess getExceptionProcess() {
		return exceptionProcess;
	}

	public void setExceptionProcess(ExceptionProcess exceptionProcess) {
		this.exceptionProcess = exceptionProcess;
	}

	public NameResolver getNameResolver() {
		return nameResolver;
	}

	public void setNameResolver(NameResolver nameResolver) {
		this.nameResolver = nameResolver;
	}

}
