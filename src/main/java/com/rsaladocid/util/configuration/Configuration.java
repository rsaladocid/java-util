package com.rsaladocid.util.configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

/**
 * This class consists exclusively of static methods that operate on objects to
 * manage configuration values as <i>key-value pairs</i>.
 */
public class Configuration {

	private final static String SETTER = "set";
	private final static String GETTER = "get";

	private Configuration() {

	}

	/**
	 * <p>
	 * Extracts all values provided by the public getter methods of the given object
	 * as <i>key-value pairs</i>. Note that a getter method is considered any method
	 * whose name starts with <i>get</i> and has zero arguments.
	 * </p>
	 * <p>
	 * Example:
	 * </p>
	 * 
	 * <pre>
	 * <code>
	 * public class Person {
	 *     
	 *     private String name;
	 *     
	 *     public Person(String name) {
	 *         this.name = name;
	 *     }
	 *     
	 *     public String getName() {
	 *         return name;
	 *     }
	 *     
	 * }
	 * 
	 * Person person = new Person("Alice");
	 * Configuration.getProperties(person); // Returns: {name=Alice}
	 * </code>
	 * </pre>
	 * <p>
	 * By default, the key is defined by the getter method name (for example, the
	 * getter method called <i>getName()</i> defines the key <i>name</i>). However,
	 * the key can be manually defined using the annotation {@link Property} on the
	 * method definition:
	 * </p>
	 * 
	 * <pre>
	 * <code>
	 * {@code @Property(name = "e-mail")}
	 * public String getEmail() {
	 *     return email;
	 * }
	 * </code>
	 * </pre>
	 * <p>
	 * In this case, the key <i>e-mail</i> is defined instead of the default key
	 * <i>email</i>.
	 * </p>
	 * <p>
	 * Additionally, it is possible to ignore a specific getter method including the
	 * annotation {@link IgnoreProperty} on the method definition:
	 * </p>
	 * 
	 * <pre>
	 * <code>
	 * {@code @IgnoreProperty}
	 * public int getAge() {
	 *     return age;
	 * }
	 * </code>
	 * </pre>
	 * 
	 * @param object
	 *            the object to extract the configuration values
	 * @return a map containing the configuration values
	 */
	public static Map<String, Object> getProperties(Object object) {
		return getProperties(object, new AnnotatedPropertyNameStrategy());
	}

	/**
	 * Extracts all values provided by the public getter methods of the given object
	 * as <i>key-value pairs</i>. The key is defined according to the value returned
	 * by the given name strategy.
	 * 
	 * @param object
	 *            the object to extract the configuration values
	 * @param strategy
	 *            the strategy to define the key
	 * @return a map containing the configuration values
	 * @see Configuration#getProperties(Object)
	 */
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

	/**
	 * <p>
	 * Establishes the content of the given object using values from given
	 * <i>key-value pairs</i>.
	 * </p>
	 * <p>
	 * Example:
	 * </p>
	 * 
	 * <pre>
	 * <code>
	 * public class Person {
	 *     
	 *     private String name;
	 *     
	 *     public Person(String name) {
	 *         setName(name);
	 *     }
	 *     
	 *     public String getName() {
	 *         return name;
	 *     }
	 *     
	 *     public void setName(String name) {
	 *         this.name = name;
	 *     }
	 *     
	 * }
	 * 
	 * Person person = new Person("Alice");
	 * {@code Map<String, Object> config = new HashMap<String, Object>();}
	 * config.put("name", "Bob");
	 * Configuration.setProperties(person, config);
	 * person.getName(); // Returns: Bob
	 * </code>
	 * </pre>
	 * <p>
	 * By default, the key defines the public setter method to invoke (for example,
	 * the key <i>name</i> invokes the setter method <i>setName()</i>). Note that a
	 * setter method is considered any method whose name starts with <i>set</i> and
	 * has one argument.
	 * </p>
	 * <p>
	 * However, the key can invoke a different public setter method using the
	 * annotation {@link Property} on the method definition:
	 * </p>
	 * 
	 * <pre>
	 * <code>
	 * public void setEmail(String email) {
	 *     this.email = email;
	 * }
	 * 
	 * {@code @Property(name = "email")}
	 * public void setCanonicalEmail(String canonicalEmail) {
	 *     this.canonicalEmail = canonicalEmail;
	 * }
	 * </code>
	 * </pre>
	 * <p>
	 * In this case, the key <i>email</i> also invokes the setter method
	 * <i>setCanonicalEmail(String)</i>.
	 * </p>
	 * <p>
	 * A key is ignored if it cannot be related to any existing public setter
	 * method.
	 * </p>
	 * 
	 * @param object
	 *            the object to configure
	 * @param properties
	 *            the <i>key-value pairs</i> to configure the given object
	 */
	public static void setProperties(Object object, Map<String, Object> properties) {
		setProperties(object, properties, new AnnotatedPropertyNameStrategy());
	}

	/**
	 * Establishes the content of the given object using values from given
	 * <i>key-value pairs</i>.
	 * 
	 * @param object
	 *            the object to configure
	 * @param properties
	 *            the <i>key-value pairs</i> to configure the given object
	 * @param strategy
	 *            the strategy to define the setter method to invoke
	 * @see Configuration#setProperties(Object, Map)
	 */
	public static void setProperties(Object object, Map<String, Object> properties, PropertyNameStrategy strategy) {
		Method[] methods = object.getClass().getMethods();

		for (Method method : methods) {
			if (method.isAnnotationPresent(IgnoreProperty.class) || method.isAnnotationPresent(XmlTransient.class)) {
				continue;
			}

			if (method.getName().startsWith(SETTER) && method.getName().length() > SETTER.length()) {
				if (method.getParameterCount() == 1) {
					String name = strategy.getName(method) != null ? strategy.getName(method) : method.getName();

					if (properties.containsKey(name)) {
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
	}

}
