package com.rsaladocid.util.io;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Foo {
	
	private String name;
	private int number;
	
	public Foo() {
		setName("Foo");
		setNumber(10);
	}
	
	public String getName() {
		return name;
	}
	
	public final void setName(String name) {
		this.name = name;
	}
	
	public int getNumber() {
		return number;
	}
	
	public final void setNumber(int number) {
		this.number = number;
	}

}
