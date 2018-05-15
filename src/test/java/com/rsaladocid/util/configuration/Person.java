package com.rsaladocid.util.configuration;

public class Person {

	private String name;
	private String surnames;
	private int age;
	private String email;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurnames() {
		return surnames;
	}

	public void setSurnames(String surnames) {
		this.surnames = surnames;
	}

	@IgnoreProperty
	public int getAge() {
		return age;
	}

	@IgnoreProperty
	public void setAge(int age) {
		this.age = age;
	}

	@Property(name = "e-mail")
	public String getEmail() {
		return email;
	}

	@Property(name = "e-mail")
	public void setEmail(String email) {
		this.email = email;
	}

	public String get() {
		return "foo";
	}

	public void set(String foo) {
	}

}
