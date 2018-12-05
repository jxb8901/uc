/**
 * 2005-5-13
 */
package net.ninecube.db;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.ninecube.lang.TypedMap;


/**
 * 注意：Map中未出现或值为null的键值将不会插入或更新到数据库中
 * @author JXB
 * @TODO: 如何支持类型转换？目前全部转换为String类型，null值转转换为空字符串
 */
public class MapDBManager {
    private String tableName;
    private String[] fieldNames;
    private String[] keyFieldNames;

    public MapDBManager(String tableName, String[] fieldNames, String[] keyFieldNames) {
        this.tableName = tableName;
        this.fieldNames = fieldNames;
        this.keyFieldNames = keyFieldNames;
    }

    public int count(String where, Object[] param) {
        return database.count(where, param);
    }

    public void delete(TypedMap<String, String> o) {
        database.delete(new TypedMapEntity<String, String>(o));
    }

    public TypedMap<String, String> getById(Object id) {
        return database.getById(id);
    }

    public TypedMap<String, String> getByWhere(String where, Object[] params) {
        return database.getByWhere(where, params);
    }

    public void insert(TypedMap<String, String> o) {
        database.insert(new TypedMapEntity<String, String>(o));
    }

    public List<? extends TypedMap<String, String>> query(String where, Object[] param, int start, int count) {
        return database.query(where, param, start, count);
    }

    public List<? extends TypedMap<String, String>> queryBySql(String sql, Object[] param, int start, int count) {
        return database.queryBySql(sql, param, start, count);
    }

    /**
     * 在参数中不存在的字段不应该被更新为null
     * @param o
     */
    public void update(TypedMap<String, String> o) {
        database.update(new TypedMapEntity<String, String>(o));
    }
    
    private DBManager<TypedMapEntity<String, String>> database = new DBManager<TypedMapEntity<String, String>>() {

		protected String getTableName() {
			return tableName;
		}

		protected String[] getFields() {
			return fieldNames;
		}

		protected Object[] getFieldValues(TypedMapEntity<String, String> o) {
			return KeyedDataEx2Array(o, getFields());
		}

		protected String[] getKeyFields() {
			return keyFieldNames;
		}

		protected Object[] getKeyValues(TypedMapEntity<String, String> o) {
			return KeyedDataEx2Array(o, getKeyFields());
		}

		protected TypedMapEntity<String, String> fromArray(Object[] values) {
			TypedMapEntity<String, String> ret = new TypedMapEntity<String, String>(new TypedMap<String, String>());
            for (int i = 0; i < fieldNames.length; i++) {
                ret.put(fieldNames[i], convert(values[i]));
            }
            for (int i = 0; i < keyFieldNames.length; i++) {
                ret.put(keyFieldNames[i], convert(values[fieldNames.length+i]));
            }
            return ret;
		}
		
		protected boolean updateNull() {
			return false;
		}
        
        private Object[] KeyedDataEx2Array(TypedMap KeyedDataEx, Object[] keys) {
            Object[] ret = new Object[keys.length];
            for (int i = 0; i < keys.length; i++) {
                ret[i] = KeyedDataEx.get(keys[i]);
            }
            return ret;
        }
        
        private String convert(Object o) {
            return (o != null) ? o.toString() : "";
        }
    };
    
    private static class TypedMapEntity<K, V> extends TypedMap<K, V> implements Entity {
    	
    	private TypedMap<K, V> degelator;
    	private Entity entity = new AbstractEntity(){};
    	
    	private TypedMapEntity(TypedMap<K, V> map) { this.degelator = map; }

		public boolean isNew() {
			return entity.isNew();
		}

		public void setNew(boolean isNew) {
			entity.setNew(isNew);
		}

		public void clear() {
			degelator.clear();
		}

		public Object clone() {
			return degelator.clone();
		}

		public boolean containsKey(Object key) {
			return degelator.containsKey(key);
		}

		public boolean containsValue(Object value) {
			return degelator.containsValue(value);
		}

		public Set<Entry<K, V>> entrySet() {
			return degelator.entrySet();
		}

		public boolean equals(Object o) {
			return degelator.equals(o);
		}

		public V get(Object key) {
			return degelator.get(key);
		}

		public BigDecimal getBigDecimal(String paramName) {
			return degelator.getBigDecimal(paramName);
		}

		public boolean getBoolean(String paramName, boolean defaultValue) {
			return degelator.getBoolean(paramName, defaultValue);
		}

		public boolean getBoolean(String paramName) {
			return degelator.getBoolean(paramName);
		}

		public Date getDate(String paramName, String pattern) {
			return degelator.getDate(paramName, pattern);
		}

		public int getInt(String paramName, int defaultValue) {
			return degelator.getInt(paramName, defaultValue);
		}

		public Integer getInteger(String paramName) {
			return degelator.getInteger(paramName);
		}

		public String getString(String paramName) {
			return degelator.getString(paramName);
		}

		public String getStringTrim(String paramName) {
			return degelator.getStringTrim(paramName);
		}

		public String[] getStringValues(String paramName) {
			return degelator.getStringValues(paramName);
		}

		public int hashCode() {
			return degelator.hashCode();
		}

		public boolean isEmpty() {
			return degelator.isEmpty();
		}

		public Set<K> keySet() {
			return degelator.keySet();
		}

		public V put(K key, V value) {
			return degelator.put(key, value);
		}

		public void putAll(Map<? extends K, ? extends V> m) {
			degelator.putAll(m);
		}

		public V remove(Object key) {
			return degelator.remove(key);
		}

		public int size() {
			return degelator.size();
		}

		public String toString() {
			return degelator.toString();
		}

		public Collection<V> values() {
			return degelator.values();
		}
    }

    public String[] getFieldNames() {
        return fieldNames;
    }
    

    public void setFieldNames(String[] fieldNames) {
        this.fieldNames = fieldNames;
    }
    

    public String[] getKeyFieldNames() {
        return keyFieldNames;
    }
    

    public void setKeyFieldNames(String[] keyFieldNames) {
        this.keyFieldNames = keyFieldNames;
    }
    

    public String getTableName() {
        return tableName;
    }
    

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    

}
