package com.rsaladocid.util.configuration;

import java.lang.reflect.Method;

import javax.xml.bind.annotation.XmlElement;

public class AnnotatedPropertyNameStrategy extends PropertyNameStrategy {

	@Override
	public String getName(Method method) {
		String name = method.getName();

		if (method.isAnnotationPresent(Property.class)) {
			Property property = method.getAnnotation(Property.class);
			name = property.name();
		} else if (method.isAnnotationPresent(XmlElement.class)) {
			XmlElement property = method.getAnnotation(XmlElement.class);
			name = property.name();
		} else {
			name = name.substring(3);
		}

		if (name.length() > 1) {
			name = Character.toString(name.charAt(0)).toLowerCase() + name.substring(1);
		}

		return name;
	}

}
