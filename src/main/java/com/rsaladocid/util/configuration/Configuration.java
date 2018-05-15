package com.rsaladocid.util.configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

public class Configuration {

	private final static String SETTER = "set";
	private final static String GETTER = "get";

	private Configuration() {

	}

	public static Map<String, Object> getProperties(Object object) {
		return getProperties(object, new AnnotatedPropertyNameStrategy());
	}

	public static Map<String, Object> getProperties(Object object, PropertyNameStrategy strategy) {
		Map<String, Object> properties = new HashMap<String, Object>();

		Method[] methods = object.getClass().getMethods();

		for (Method method : methods) {
			if (method.isAnnotationPresent(IgnoreProperty.class) || method.isAnnotationPresent(XmlTransient.class)) {
				continue;
			}

			if (method.getName().startsWith(GETTER) && method.getName().length() > GETTER.length()) {
				if (method.getParameterCount() == 0) {
					try {
						Object value = method.invoke(object);
						String name = strategy.getName(method) != null ? strategy.getName(method) : method.getName();
						properties.put(name, value);
					} catch (IllegalArgumentException e) {

					} catch (IllegalAccessException e) {

					} catch (InvocationTargetException e) {

					}
				}
			}
		}

		return properties;
	}

	public static Object setProperties(Object object, Map<String, Object> properties) {
		return setProperties(object, properties, new AnnotatedPropertyNameStrategy());
	}

	public static Object setProperties(Object object, Map<String, Object> properties, PropertyNameStrategy strategy) {
		Method[] methods = object.getClass().getMethods();

		for (Method method : methods) {
			if (method.isAnnotationPresent(IgnoreProperty.class) || method.isAnnotationPresent(XmlTransient.class)) {
				continue;
			}

			if (method.getName().startsWith(SETTER) && method.getName().length() > SETTER.length()) {
				if (method.getParameterCount() == 1) {
					String name = strategy.getName(method) != null ? strategy.getName(method) : method.getName();

					if (properties != null && properties.containsKey(name)) {
						try {
							method.invoke(object, new Object[] { properties.get(name) });
						} catch (IllegalArgumentException e) {

						} catch (IllegalAccessException e) {

						} catch (InvocationTargetException e) {

						}
					}
				}
			}
		}

		return object;
	}

}
