package com.jofkos.signs.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class Reflect {
	
	public static void set(Object o, String field, Object value) throws Exception {
		Field f = getField(o, field);
		f.setAccessible(true);
		f.set(o, value);
	}
	
	public static void set(Class<?> c, String field, Object value) throws Exception {
		Field f = getField(c, field);
		f.setAccessible(true);
		f.set(null, value);
	}
	
	public static <T> T get(Object o, String field) throws Exception {
		Field f = getField(o, field);
		f.setAccessible(true);
		return (T) f.get(o);
	}
	
	public static <T> T get(Class<?> c, String field) throws Exception {
		Field f = getField(c, field);
		f.setAccessible(true);
		return (T) f.get(null);
	}
	
	private static Field getField(Object o, String field) throws Exception {
		return getField(o.getClass(), field);
	}
	
	private static Field getField(Class<?> c, String field) throws Exception {
		try {
			return c.getDeclaredField(field);
		} catch (NoSuchFieldException e) {
			return c.getField(field);
		}
	}
	
//	----------------------------------- Methods -----------------------------------	\\
	
	public static <T> T invoke(Object o, String method, Object... args) throws Exception {
		Method m = o.getClass().getMethod(method, getClasses(args));
		return (T) m.invoke(o, args);
	}
	
	public static <T> T invoke(Object o, String method, Object arg, Class<?> c) throws Exception {
		Method m = o.getClass().getMethod(method, c);
		return (T) m.invoke(o, arg);
	}
	
	public static <T> T invoke(Object o, String method, Object[] args, Class<?>[] classes) throws Exception {
		Method m = o.getClass().getMethod(method, classes);
		return (T) m.invoke(o, args);
	}
	
	public static Class<?>[] getClasses(Object... o) {
		List<Class<?>> params = new ArrayList<Class<?>>();
		for (Object c : o) {
			if (c.getClass() == Integer.class) {
				params.add(Integer.TYPE);
			} else {
				params.add(c.getClass());
			}
		}
		return params.toArray(new Class<?>[o.length]);
	}
	
}