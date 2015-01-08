# JSoap

JSoap is an Android library which allows automatic handling of SOAP requests and resposes, making the process effortless to the user. 

## How it works
The JSoap library makes SOAP requests and following replies very easy to handle. Create a class with the request paramenters and a class with the expected output fields. JSoap will do the rest for you!

### Request class
The request class ***MUST*** extend from **SOAPSerializable**. Each field you want to add to the request message must have the **@JSoapField** annotation. Each @JSoapField must have at least the order of the field. This is important because in SOAP, field order matters and can't be random, and there's no common way to have the fields ordered by appearence in the class they're in. Other @JSoapField parameters that can be relevant are **fieldName** (if you want the request field to have a different name than the one in the class field), and a **namespace**. If you add the **JSoapClass** annotation providing the namespace on top of your class, then this namespace will be used on all fields except those that have the namespace on their @JSoapField annotation.

For those cases when you want to add an attribute, you'll just need to add the **@JSoapAttribute** annotation to the desired field. In this case you may also provide an alternative name.

The following class...

```java
JSoapClass(namespace = "http://tempuri.org/")
public class MyInput extends SOAPSerializable {

    @JSoapField(order = 0)
    private String theUsername;
    
    @JSoapField(order = 1)
    private String thePassword;
    
    @JSoapAttribute(name="myAttribute")
    private String attribute;
    
}

```
... or the changed version of itself ...

```java
public class MyInput extends SOAPSerializable {

    @JSoapField(namespace = "http://tempuri.org/", order = 0, fieldName="theUsername")
    private String user;
    
    @JSoapField(namespace = "http://tempuri.org/", fieldName = "thePassword", order = 1)
    private String password;
    
    @JSoapAttribute(namespace = "http://tempuri.org/")
    private String myAttribute;
    
}

```

will turn into 

```xml
<MyInput myAttribute="<something>" namespace = "http://tempuri.org/">
	<theUsername> <!-- username --> </theUsername>
	<thePassword> <!-- password --> </thePassword>
</MyInput>
```


###Response class


###Making the request



## Add JSoap to your project
Since JSoap is on Maven Central, adding it to your project is as simple as adding one of the following snippets for Maven or Gradle projects.

```
Soon to be released!
```

