package com.rsaladocid.util.configuration;

import java.lang.reflect.Method;

public abstract class PropertyNameStrategy {

	public abstract String getName(Method method);

}
