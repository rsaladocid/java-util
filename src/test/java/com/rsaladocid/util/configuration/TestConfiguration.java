package com.rsaladocid.util.configuration;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class TestConfiguration {

	private static final String NAME = "test";
	private static final String SURNAMES = "foo stub";
	private static final int AGE = 15;
	private static final String EMAIL = "bar@foo.com";
	private static final String CANONICAL_EMAIL = "bar@foo.com";

	private Map<String, Object> properties;
	private Person person;

	@Before
	public void setUp() throws Exception {
		properties = new HashMap<String, Object>();
		properties.put("name", NAME);
		properties.put("surnames", SURNAMES);
		properties.put("age", AGE);
		properties.put("e-mail", EMAIL);

		person = new Person();
		person.setName(NAME);
		person.setSurnames(SURNAMES);
		person.setAge(AGE);
		person.setEmail(EMAIL);
	}

	@Test
	public void testGetProperties() {
		Map<String, Object> properties = Configuration.getProperties(person);

		assertTrue(properties.keySet().contains("name"));
		assertTrue(properties.keySet().contains("surnames"));
		assertTrue(properties.keySet().contains("e-mail"));
		assertTrue(properties.keySet().contains("canonicalEmail"));
	}

	@Test
	public void testGetRenamedProperty() {
		assertTrue(Configuration.getProperties(person).get("e-mail").equals(EMAIL));
	}

	@Test
	public void testGetIgnoredProperty() {
		assertTrue(Configuration.getProperties(person).get("age") == null);
	}

	@Test
	public void testSetProperties() {
		Person test = new Person();
		Configuration.setProperties(test, properties);

		assertTrue(test.getName().equals(NAME));
		assertTrue(test.getAge() == 0);
		assertTrue(test.getEmail().equals(EMAIL));
		assertTrue(test.getSurnames().equals(SURNAMES));
	}

	@Test
	public void testSetRenamedProperties() {
		Person test = new Person();
		Configuration.setProperties(test, properties);

		assertTrue(test.getCanonicalEmail().equals(CANONICAL_EMAIL));
	}

}
