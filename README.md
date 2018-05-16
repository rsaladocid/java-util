com.rsaladocid.util
===================

Collection of useful reusable Java code.

- [Configuration API](#configuration-api)
- [I/O API](#io-api)

Configuration API
-----------------

To configure objects using key-value pairs.

### Basic example

Here's a simple `Person` class:

```java
public class Person {
	private String name;
	private String email;

	public Person(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
}
```

And this is how to easily get its properties as key-value pairs:

```java
Person person = new Person("Alice");
person.setEmail("alice@geemail.com");
Configuration.getProperties(person); // Returns: {name=Alice,email=alice@geemail.com}
```

Moreover, you can also establish its properties from key-value pairs:

```java
	Person person = new Person("Alice");
	Map<String, Object> config = new HashMap<String, Object>();
	config.put("name", "Bob");
	config.put("email", "bob@geemail.com");

	Configuration.setProperties(person, config);
	person.getName(); // Returns: Bob
	person.getEmail(); // Returns: bob@geemail.com
```

### Advanced example

Here's an annotated `Person` class:

```java
public class Person {
    private String name;
    private String surnames;
    private String email;

    public Person(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }
    public String getSurnames() {
        return surnames;
    }
    @IgnoreProperty
    public String getFullName() {
        return getName() + " " + getSurnames();
    }
    @Property(name = "e-mail")
    public void setEmail(String email) {
        this.email = email;
    }
    @Property(name = "e-mail")
    public String getEmail() {
        return email;
    }
}
```

Now, the property `fullName` is ignored and the property `email` is renamed to `e-mail`.

```java
	Person person = new Person("Alice");
	person.setSurnames("Foo Stub");
	person.setEmail("alice@geemail.com");
	
	person.getFullName(); // Returns: Alice Foo Stub
	
	Configuration.getProperties(person); // Returns: {name=Alice,surnames=Foo Stub,e-mail=alice@geemail.com}
```

```java
	Person person = new Person("Alice");
	Map<String, Object> config = new HashMap<String, Object>();
	config.put("name", "Bob");
	config.put("surnames", "Stub Foo");
	config.put("e-mail", "bob@geemail.com");

	Configuration.setProperties(person, config);
	person.getName(); // Returns: Bob
	person.getSurnames(); // Returns: Stub Foo
	person.getEmail(); // Returns: bob@geemail.com
```

I/O API
-------

A simple common API to serialize objects to JSON or XML objects.

### Basic example

Here's a simple `Person` class:

```java
public class Person {
	private String name;
	private String email;

	public Person(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
}
```

And this is how to easily convert a `Person` object to a JSON object:

```java
Person person = new Person("Alice");
person.setEmail("alice@geemail.com");
Serializer.build().toJson(person, System.out); // Returns: {"person":{"name":"Alice","email":"alice@geemail.com"}}
```

...or to a XML object:

```java
Person person = new Person("Alice");
person.setEmail("alice@geemail.com");
Serializer.build().toXml(person, System.out); // Returns: <?xml version="1.0" encoding="UTF-8"?><person><name>Alice</name><email>alice@geemail.com</email></person>
```

Moreover, you can also convert a JSON object to the corresponding object:

```java
	Person person = (Person) Serializer.build().fromJson(stream, Person.class);
	person.getName(); // Returns: Alice
	person.getEmail(); // Returns: alice@geemail.com
```

...or to convert a XML object to the corresponding object:

```java
	Person person = (Person) Serializer.build().fromXml(stream, Person.class);
	person.getName(); // Returns: Alice
	person.getEmail(); // Returns: alice@geemail.com
```

License
-------
Code is under the [MIT License](https://opensource.org/licenses/MIT)