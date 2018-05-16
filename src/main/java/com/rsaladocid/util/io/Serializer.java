package com.rsaladocid.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class converts objects to XML or JSON objects. After a serialized object
 * has been written, it can be read and deserialized that is.
 */
public class Serializer {

	/**
	 * Values to configure the serializer.
	 * 
	 * @see Marshaller
	 * @see MarshallerProperties
	 */
	private Map<String, Object> properties;

	/**
	 * Constructs a default serializer
	 * 
	 * @return the serializer
	 */
	public static Serializer build() {
		return new Serializer();
	}

	/**
	 * Constructs a serializer configured with the given properties
	 * 
	 * @param properties
	 *            the values to configure the serializer
	 * @return the serializer
	 */
	public static Serializer build(Map<String, Object> properties) {
		return new Serializer(properties);
	}

	/**
	 * Creates a default serializer.
	 */
	public Serializer() {
		this(new HashMap<String, Object>());
	}

	/**
	 * Create a serializer with the given configuration.
	 * 
	 * @param properties
	 *            the values to configure the serializer
	 */
	public Serializer(Map<String, Object> properties) {
		setProperties(properties);
	}

	/**
	 * <p>
	 * Converts a given object to a JSON object. By default, a JSON root element is
	 * included.
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
	 * Serializer.build().toJson(person, System.out); // Returns: {"person":{"name":"Alice"}}
	 * </code>
	 * </pre>
	 * 
	 * @param object
	 *            the object to convert to a JSON object
	 * @param output
	 *            the output stream where the serialized object is stored
	 * @throws JAXBException
	 * @throws IOException
	 */
	public void toJson(Object object, OutputStream output) throws JAXBException, IOException {
		if (object instanceof Map) {
			ObjectMapper mapper = new ObjectMapper();

			if (getProperties().get(Marshaller.JAXB_FORMATTED_OUTPUT) != null
					&& getProperties().get(Marshaller.JAXB_FORMATTED_OUTPUT).toString() == Boolean.TRUE.toString()) {
				try {
					mapper.writerWithDefaultPrettyPrinter().writeValue(output, object);
				} catch (JsonMappingException e) {
					throw new JAXBException(e);
				}
			} else {
				try {
					mapper.writeValue(output, object);
				} catch (JsonMappingException e) {
					throw new JAXBException(e);
				}
			}
		} else {
			Marshaller marshaller = createMarshaller(object.getClass(), getProperties());
			marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");

			marshaller.marshal(object, output);
		}
	}

	/**
	 * <p>
	 * Reads a JSON object from an input stream of bytes and creates the
	 * corresponding object.
	 * </p>
	 * 
	 * <pre>
	 * <code>
	 * Person person = (Person) Serializer.build().fromJson(stream, Person.class);
	 * person.getName(); // Returns: Alice
	 * </code>
	 * </pre>
	 * 
	 * @param input
	 *            the input stream to read the JSON object
	 * @param classToBeBound
	 *            the corresponding object class
	 * @return the deserialized object
	 * @throws JAXBException
	 * @throws IOException
	 */
	public Object fromJson(InputStream input, Class<?> classToBeBound) throws JAXBException, IOException {
		if (classToBeBound.isAssignableFrom(Map.class)) {
			ObjectMapper mapper = new ObjectMapper();

			return mapper.readValue(input, classToBeBound);
		} else {
			Unmarshaller unmarshaller = createUnmarshaller(classToBeBound, getProperties());
			unmarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");

			return unmarshaller.unmarshal(input);
		}
	}

	/**
	 * <p>
	 * Converts a given object to a XML object.
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
	 * {@code Serializer.build().toJson(person, System.out); // Returns: <?xml version="1.0"
	 * encoding="UTF-8"?><person><name>Alice</name></person>}
	 * </code>
	 * </pre>
	 * 
	 * @param object
	 *            the object to convert to a XML object
	 * @param output
	 *            the output stream where the serialized object is stored
	 * @throws JAXBException
	 * @throws IOException
	 */
	public void toXml(Object object, OutputStream output) throws JAXBException, IOException {

		Marshaller marshaller = createMarshaller(object.getClass(), getProperties());
		marshaller.marshal(object, output);
	}

	/**
	 * <p>
	 * Reads a XML object from an input stream of bytes and creates the
	 * corresponding object.
	 * </p>
	 * 
	 * <pre>
	 * <code>
	 * Person person = (Person) Serializer.build().fromXml(stream, Person.class);
	 * person.getName(); // Returns: Alice
	 * </code>
	 * </pre>
	 * 
	 * @param input
	 *            the input stream to read the XML object
	 * @param classToBeBound
	 *            the corresponding object class
	 * @return the deserialized object
	 * @throws JAXBException
	 * @throws IOException
	 */
	public Object fromXml(InputStream input, Class<?> classToBeBound) throws JAXBException, IOException {
		Unmarshaller unmarshaller = createUnmarshaller(classToBeBound, getProperties());

		return unmarshaller.unmarshal(input);
	}

	/**
	 * Returns the properties to configure the serializer.
	 * 
	 * @return the configuration values
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}

	/**
	 * Defines the values to configure the serializer.
	 * 
	 * @param properties
	 */
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	/**
	 * Creates the marshaller that serializes objects
	 * 
	 * @param classToBeBound
	 *            the class of the object to serialize
	 * @param properties
	 *            the values to configure the marshaller
	 * @return the marshaller
	 * @throws JAXBException
	 */
	protected Marshaller createMarshaller(Class<?> classToBeBound, Map<String, ?> properties) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(classToBeBound);
		Marshaller marshaller = context.createMarshaller();

		Iterator<String> iterator = getProperties().keySet().iterator();
		while (iterator.hasNext()) {
			String property = iterator.next();
			marshaller.setProperty(property, getProperties().get(property));
		}

		return marshaller;
	}

	/**
	 * Creates the unmarshaller that deserializes objects
	 * 
	 * @param classToBeBound
	 *            the class of the object to deserializer
	 * @param properties
	 *            the values to configure the unmarshaller
	 * @return the unmarshaller
	 * @throws JAXBException
	 */
	protected Unmarshaller createUnmarshaller(Class<?> classToBeBound, Map<String, ?> properties) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(classToBeBound);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		Iterator<String> iterator = getProperties().keySet().iterator();
		while (iterator.hasNext()) {
			String property = iterator.next();
			unmarshaller.setProperty(property, getProperties().get(property));
		}

		return unmarshaller;
	}

}
