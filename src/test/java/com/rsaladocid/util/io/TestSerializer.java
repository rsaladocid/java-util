package com.rsaladocid.util.io;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.junit.Test;

public class TestSerializer {

	private String json = "{" + "\"foo\":" + "{" + "\"name\":\"Foo\"," + "\"number\":10" + "}" + "}";

	private String xml = "" + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<foo>" + "<name>Foo</name>"
			+ "<number>10</number>" + "</foo>";

	@Test
	public void testXmlObjectSerialization() throws JAXBException, IOException {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, false);

		Serializer serializer = new Serializer(properties);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		serializer.toXml(new Foo(), stream);

		assertTrue(stream.toString().equals(xml));
	}

	@Test
	public void testXmlObjectDeserialization() throws JAXBException, IOException {
		Serializer serializer = new Serializer();
		ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes());

		Foo foo = new Foo();
		Object result = serializer.fromXml(stream, foo.getClass());

		assertTrue(result.getClass().equals(foo.getClass()));
		assertTrue(((Foo) result).getName().equals("Foo"));
		assertTrue(((Foo) result).getNumber() == 10);
	}

	@Test
	public void testJsonObjectSerialization() throws JAXBException, IOException {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, false);

		Serializer serializer = new Serializer(properties);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		serializer.toJson(new Foo(), stream);

		assertTrue(stream.toString().equals(json));
	}

	@Test
	public void testJsonObjectDeserialization() throws JAXBException, IOException {
		Serializer serializer = new Serializer();
		ByteArrayInputStream stream = new ByteArrayInputStream(json.getBytes());

		Foo foo = new Foo();
		Object result = serializer.fromJson(stream, foo.getClass());

		assertTrue(result.getClass().equals(foo.getClass()));
		assertTrue(((Foo) result).getName().equals("Foo"));
		assertTrue(((Foo) result).getNumber() == 10);
	}

	@Test
	public void testJsonMapStringObjectSerialization() throws JAXBException, IOException {
		Serializer serializer = new Serializer();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("foo", new Foo());

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		serializer.toJson(map, stream);

		assertTrue(stream.toString().equals(json));
	}

}
