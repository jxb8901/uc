package net.ninecube.saturn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ninecube.db.DBUtil;
import net.ninecube.saturn.database.Column;
import net.ninecube.saturn.database.DatabaseException;
import net.ninecube.saturn.function.FunctionDefinition;
import net.ninecube.saturn.function.FunctionProvider;
import net.ninecube.util.GenericsUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NameResolverProvider {
	
	private static List<NameResolverBase> resolvers = new ArrayList<NameResolverBase>();
	static{
		resolvers.add(new DateDataNameResolver());
		resolvers.add(new DBMappedNameResolver("积分.", "select name from TPointType where name = ?", "name", "name"));
		resolvers.add(new ColumnNameResolver());
		resolvers.add(new MetricReferenceResolvor());
	}
	
	@SuppressWarnings("unchecked")
	public static <T> NameResolver<T> getResolver(Class<T> type){
		ChainNameResolver<T> r = new ChainNameResolver<T>(type);
		for(NameResolverBase br : resolvers)
			if(type.isAssignableFrom(br.getResultType())) r.addResolver(br);
		return r;
	}
	
	public static NameResolver<Object> getDefaultChainResolver(){
		return getResolver(Object.class);
	}
	
	
	
	public static abstract class NameResolverBase<T> implements NameResolver<T>{

		public boolean validate(Context context, String name){
			return resolve(context,name) != null;
		}
		
		public Class getResultType(){
			return GenericsUtils.getGenericClass(this.getClass());
		}
	}

	public static class DateDataNameResolver  extends NameResolverBase<DateData>{
		public DateData resolve(Context context, String name){
			try {
				return DateData.parse(name);
			} catch (IllegalArgumentException e) {
			}
			return null;
		}
	}
	
	public static class DBMappedNameResolver  extends NameResolverBase<String>{
		private String sql;
		private String originalField, targetField;
		private String prefix;
		
		public DBMappedNameResolver(String prefix, String sql, String ofld, String tFld){
			this.prefix = prefix;
			this.sql = sql;
			this.originalField = ofld;
			this.targetField = tFld;
			
		}
		
		public String resolve(Context context, String name){
			if(!name.startsWith(prefix)) return null;
			String sname = name.substring(prefix.length());
			String[] flds;
			if(this.targetField.equals(this.originalField)) flds =  new String[]{targetField,};
			else flds =  new String[]{originalField, targetField};
			Map<String, Object> m =DBUtil.select(sql, new String[]{sname},flds);
			if(m.isEmpty()) return null;
			return m.get(targetField).toString();
		}
	}
	
	public static class ColumnNameResolver extends NameResolverBase<Column>{
		public Column resolve(Context context, String name){
			//if(!name.contains(".")) return null;
			try {
				return context.getDataSet().getColumnByAlias(name);
			} catch (DatabaseException e) {
				return null;
			} 
		}
	}
	
	public static class ConstantNameResolver extends NameResolverBase<String>{
		private List<String> constants = new ArrayList<String>();
		private Log log = LogFactory.getLog(ConstantNameResolver.class);
		public ConstantNameResolver(String[] constants){
			this.constants.addAll(Arrays.asList(constants));
		}
		public String resolve(Context context, String name){
			log.debug("**resolver name : " + name + " result :" + constants.contains(name) + " ; constants : " + constants);
			return constants.contains(name) ? name : null;
		}
	}
	
	public static class MetricReferenceResolvor extends NameResolverBase<Column>{
		private Log log = LogFactory.getLog(MetricReferenceResolvor.class);
		
		public boolean validate(Context context, String name){
			FunctionDefinition fd = FunctionProvider.getInstance().getByName(name);
			return fd != null;
		}
		
		public Column resolve(Context context, String name){
			FunctionDefinition fd = FunctionProvider.getInstance().getByName(name);
			log.debug("locate name : " + name);
			if(fd == null ) return null;
			return (Column)fd.getFunction().execute(context, new ArrayList(), new HashMap());
		}
	}
	
	public static class ChainNameResolver<T> extends NameResolverBase<T>{
		private List<NameResolver<? extends T>> chain = new ArrayList<NameResolver<? extends T>>();
		Class<T> type;
		
		public ChainNameResolver(Class<T> type){
			this.type = type;
		}
		
		public boolean validate(Context context, String name){
			for(NameResolver<? extends T> r : chain){
				if(r.validate(context, name)) return true;
			}
			return false;
		}
		
		public T resolve(Context context, String name){
			for(NameResolver<? extends T> r : chain){
				T rst = r.resolve(context, name);
				if(rst != null) return rst;
			}
			return null;
		}
		
		public Class getResultType(){
			return type;
		}
		
		public ChainNameResolver<T> addResolver(NameResolver<? extends T> r){
			chain.add(r);
			return this;
		}
		
		public void clear(){
			chain.clear();
		}
	}
	
	public static void main(String[] args){
		System.out.println(new ColumnNameResolver().getResultType());
	}

}
