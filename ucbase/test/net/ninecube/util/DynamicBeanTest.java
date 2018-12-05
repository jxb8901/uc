/**
 * 
 * created on 2007-1-23
 */
package net.ninecube.util;

import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author jxb
 * 
 */
public class DynamicBeanTest extends TestCase {
	
	private String className = DynamicBeanTest.class.getPackage().getName() + ".Example";
	private List<String> fieldNames = new ArrayList<String>();
	private List<Class> fieldTypes = new ArrayList<Class>();
	
	protected void setUp() {
		fieldNames.add("id");
		fieldTypes.add(Long.class);
		fieldNames.add("name");
		fieldTypes.add(String.class);
		fieldNames.add("date");
		fieldTypes.add(Date.class);
	}

	public void testCreateDynamicBeanWithPublicField() throws Exception {
		Class c = DynamicBean.createDynamicBeanWithPublicField(className+"1", fieldNames, fieldTypes);
		this.assertEquals(className+"1", c.getName());
		Field[] fs = c.getFields();
		this.assertEquals(3, fs.length);
		this.assertEquals("id", fs[0].getName());
		this.assertEquals("name", fs[1].getName());
		this.assertEquals("date", fs[2].getName());
	}

	@SuppressWarnings("unchecked")
	public void testCreateDynamicBean() throws Exception {
		Class c = DynamicBean.createDynamicBean(className+"2", fieldNames, fieldTypes);
		Field[] fs = c.getFields();
		this.assertEquals(0, fs.length);
		Method m = c.getMethod("getId", new Class[0]);
		this.assertNotNull(m);
		m = c.getMethod("setId", new Class[]{Long.class});
		this.assertNotNull(m);
	}
	
	public void testGetterAndSetter() {
		TestBean bean = new TestBean();
		this.assertNull(bean.get("id"));
		this.assertNull(bean.get("name"));
		this.assertNull(bean.get("date"));
		bean.set("id", 2L);
		bean.set("name", "test");
		bean.set("date", new Date());
		this.assertEquals(2L, bean.get("id"));
		this.assertEquals("test", bean.get("name"));
		this.assertNotNull(bean.get("date"));
		this.assertEquals(Date.class, bean.get("date").getClass());
	}
	
	public void testToMap() {
		TestBean bean = new TestBean();
		bean.set("id", 5L);
		bean.set("name", "test11");
		bean.set("date", new Date());
		Map<String, Object> map = bean.toMap();
		this.assertEquals(3, map.size());
		this.assertEquals(5L, map.get("id"));
		this.assertEquals("test11", map.get("name"));
		this.assertNotNull(map.get("date"));
		this.assertEquals(Date.class, map.get("date").getClass());
	}
	
	public void testFromMap() {
		TestBean bean = new TestBean();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", 5L);
		map.put("name", "test11");
		map.put("date", new Date());
		bean.fromMap(map);
		this.assertEquals(5L, bean.get("id"));
		this.assertEquals("test11", bean.get("name"));
		this.assertNotNull(bean.get("date"));
		this.assertEquals(Date.class, bean.get("date").getClass());
		
		map.remove("id");
		map.put("date", null);
		map.put("name", "test12");
		bean.fromMap(map);
		this.assertEquals(5L, bean.get("id"));
		this.assertEquals("test12", bean.get("name"));
		this.assertNotNull(bean.get("date"));
	}
	
	public void testSuperClass() {
		Class c = DynamicBean.createDynamicBean(className+"3", fieldNames, fieldTypes);
		this.assertTrue(DynamicBean.class.isAssignableFrom(c));
	}
	
	//~
	
	protected static void writeClassFile(ClassWriter cw, String className) {
		byte[] code = cw.toByteArray();
        try {
			FileOutputStream fos = new FileOutputStream(className + ".class");
			fos.write(code);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	protected static void invokeMethod0(Class clazz) throws IllegalArgumentException, 
			SecurityException, IllegalAccessException, InvocationTargetException {
		clazz.getMethods()[0].invoke(null, new Object[] { null });
	}
	
	/**
	 * public static void main (String[] args) {
     * 	System.out.println("Hello world!");
     * }
	 */
	protected static void createHelloWorldMethod(ClassWriter cw) {
		// creates a MethodWriter for the 'main' method
		MethodVisitor mw = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
                "main",
                "([Ljava/lang/String;)V",
                null,
                null);
        // pushes the 'out' field (of type PrintStream) of the System class
        mw.visitFieldInsn(Opcodes.GETSTATIC,
                "java/lang/System",
                "out",
                "Ljava/io/PrintStream;");
        // pushes the "Hello World!" String constant
        mw.visitLdcInsn("Hello world!");
        // invokes the 'println' method (defined in the PrintStream class)
        mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                "java/io/PrintStream",
                "println",
                "(Ljava/lang/String;)V");
        mw.visitInsn(Opcodes.RETURN);
        // this code uses a maximum of two stack elements and two local
        // variables
        mw.visitMaxs(2, 2);
        mw.visitEnd();
	}


    public static void main1(final String args[]) throws Exception {
    	Class exampleClass = null;
    	long time = System.currentTimeMillis();
    	for (int i = 0; i < 99999; i++) {
	        ClassWriter cw = DynamicBean.createClassWriter("Example" + i);
	        DynamicBean.createDefaultConstructor(cw);
	        createHelloWorldMethod(cw);
	        exampleClass = DynamicBean.createClass(cw, "Example" + i);
    	}
    	time = System.currentTimeMillis() - time;
    	System.out.println("****" + time + "," + (time / 99999));
        invokeMethod0(exampleClass);
        
        System.out.println(DynamicBeanTest.class.getPackage().getName() + ".Example");
        System.out.println(exampleClass.getName());
    }
    
    private static class TestBean extends DynamicBean {
    	private static final String SF = "SF";
    	private static String S = "S";
    	private final String F = "F";
    	private Long id;
    	public String name;
    	private Date date;
    	
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
    	
    }
    
    public static void main(String args[]) throws Exception {
    	String className = "Example";
    	List<String> fieldNames = new ArrayList<String>();
    	List<Class> fieldTypes = new ArrayList<Class>();
    	fieldNames.add("id");
    	fieldTypes.add(Long.class);
    	fieldNames.add("name");
    	fieldTypes.add(String.class);

    	Class c = null;
    	long time = System.currentTimeMillis();
    	for (int i = 0; i < 9999999; i++) {
    		c = DynamicBean.createDynamicBean(className + i, fieldNames, fieldTypes);
    		c.newInstance();
    	}
    	time = System.currentTimeMillis() - time;
    	System.out.println("****" + time + "," + (time / 9999999));
//    	for (Field f : c.getFields()) {
//    		System.out.println(f.getModifiers() + "," + f.getName() + "," + f.getType());
//    	}
//    	for (Method m : c.getMethods()) {
//    		System.out.println(m.getModifiers() + "," + m.getReturnType() + "," + m.getName() + "," + Arrays.asList(m.getParameterTypes()));
//    	}
    }
}
