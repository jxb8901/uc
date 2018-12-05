/**
 * 
 * created on 2007-1-22
 */
package net.ninecube.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * 
 * @author jxb
 * 
 */
public class DynamicBean implements Serializable {
	private static final String SUPERNAME = DynamicBean.class.getName().replace('.', '/');
	
	/**
	 * 将动态BEAN的属性以MAP形式返回，NULL值不返回 
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> ret = new HashMap<String, Object>();
		for (Field f : this.getClass().getDeclaredFields()) {
			try {
				// 忽略static或final值
				if (Modifier.isStatic(f.getModifiers()) || 
						Modifier.isFinal(f.getModifiers())) continue;
				f.setAccessible(true);
				ret.put(f.getName(), f.get(this));
			} catch (Exception e) { } 
		}
		return ret;
	}
	
	/**
	 * 从map中拷贝状态，NULL值不拷贝
	 */
	public void fromMap(Map<String, Object> map) {
		for (Field f : this.getClass().getDeclaredFields()) {
			try {
				// 忽略static或final值
				if (Modifier.isStatic(f.getModifiers()) || Modifier.isFinal(f.getModifiers()))
					continue;
				if (map.containsKey(f.getName()) && map.get(f.getName()) != null) {
					f.setAccessible(true);
					f.set(this, map.get(f.getName()));
				}
			} catch (Exception e) {
			}
		}
	}
	
	public Object get(String name) {
		try {
			Field f = this.getClass().getDeclaredField(name);
			f.setAccessible(true);
			return f.get(this);
		} catch (NoSuchFieldException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void set(String name, Object value) {
		try {
			Field f = this.getClass().getDeclaredField(name);
			f.setAccessible(true);
			f.set(this, value);
		} catch (NoSuchFieldException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建一个动态BEAN，该BEAN的所有字段全部为公有的，没有getter和setter方法
	 * @param className 要创建BEAN的类名，形如：net.ninecube.util.Example
	 * @fieldNames bean属性名称
	 * @param fieldTypes bean属性类型
	 */
	public static Class createDynamicBeanWithPublicField(String className, List<String> fieldNames, List<Class> fieldTypes) {
        ClassWriter cw = createClassWriter(className);
        createDefaultConstructor(cw);
        for (int i = 0; i < fieldNames.size(); i++) {
        	createPublicField(cw, fieldNames.get(i), fieldTypes.get(i));
        }
        return createClass(cw, className);
	}

	/**
	 * 创建一个动态BEAN，并且生成相应的getter和setter方法
	 * @param className 要创建BEAN的类名，形如：net.ninecube.util.Example
	 * @fieldNames bean属性名称
	 * @param fieldTypes bean属性类型
	 */
	public static Class createDynamicBean(String className, List<String> fieldNames, List<Class> fieldTypes) {
        ClassWriter cw = createClassWriter(className);
        createDefaultConstructor(cw);
        for (int i = 0; i < fieldNames.size(); i++) {
        	createPrivateField(cw, className, fieldNames.get(i), fieldTypes.get(i));
        }
        return createClass(cw, className);
	}
	
	//~ private method
	
	protected static void createPrivateField(ClassWriter cw, String className, String propertyName, Class type) {
		FieldVisitor fv = cw.visitField(Opcodes.ACC_PRIVATE, propertyName, Type.getDescriptor(type), null, null);
		fv.visitEnd();
    	createGetter(cw, className, propertyName, type);
    	createSetter(cw, className, propertyName, type);
	}
	
	protected static void createPublicField(ClassWriter cw, String propertyName, Class type) {
		FieldVisitor fv = cw.visitField(Opcodes.ACC_PUBLIC, propertyName, Type.getDescriptor(type), null, null);
		fv.visitEnd();
	}
	
	protected static void createSetter(ClassWriter cw, String className, String propertyName, Class c) {
		String methodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		String type = Type.getDescriptor(c);
		MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, methodName, "(" + type + ")V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Type.getType(c).getOpcode(Opcodes.ILOAD), 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, className.replace('.', '/'), propertyName, type);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}

	protected static void createGetter(ClassWriter cw, String className, String propertyName, Class c) {
		String methodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		String returnType = Type.getDescriptor(c);
		MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, methodName, "()" + returnType, null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, className.replace('.', '/'), propertyName, returnType);
		mv.visitInsn(Type.getType(c).getOpcode(Opcodes.IRETURN));
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}
	
	protected static void createDefaultConstructor(ClassWriter cw) {
		MethodVisitor mw = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        // pushes the 'this' variable
        mw.visitVarInsn(Opcodes.ALOAD, 0);
        // invokes the super class constructor
        mw.visitMethodInsn(Opcodes.INVOKESPECIAL, SUPERNAME, "<init>", "()V");
        mw.visitInsn(Opcodes.RETURN);
        // this code uses a maximum of one stack element and one local variable
        mw.visitMaxs(0, 0);
        mw.visitEnd();
	}
	
	/**
	 * creates a ClassWriter for the Example public class,
	 * which inherits from Object
	 * public class ClassName {
	 * 
	 * }
	 */
	protected static ClassWriter createClassWriter(String className) {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cw.visit(Opcodes.V1_1, Opcodes.ACC_PUBLIC, className.replace('.', '/'), null, SUPERNAME, null);
        return cw;
	}
	
	protected static Class createClass(final ClassWriter cw, final String className) {
		return DEFAULTCLASSLOADER.createClass(className, cw.toByteArray());
	}
	
	private static final MyClassLoader DEFAULTCLASSLOADER = new MyClassLoader(DynamicBean.class.getClassLoader());
	
	private static class MyClassLoader extends ClassLoader {
		public MyClassLoader() {
			super();
		}
		public MyClassLoader(ClassLoader parent) {
			super(parent);
		}
		public Class createClass(String className, byte[] code) {
			return this.defineClass(className, code, 0, code.length);
		}
	}
}
