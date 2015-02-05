# JSoap
SOAP has been losing ground to REST services in mobile the last years, thus losing attention. But SOAP isn't gone and many still use it. The thing is that it usually requires a lot of work from the dev to create and parse all the messages.  

JSoap is an Android library which allows automatic handling of SOAP requests and resposes, making the process effortless to the user. It works on top of the well-known library [KSOAP2](https://code.google.com/p/ksoap2-android/).

## How it works
The JSoap library makes SOAP requests and following replies very easy to handle. Create a class with the request paramenters and a class with the expected output fields. JSoap will do the rest for you!

### Request class
The request class ***MUST*** extend from **SOAPSerializable**. Each field you want to add to the request message must have the **@JSoapField** annotation. Each @JSoapField must have at least the order of the field. This is important because in SOAP, field order matters and can't be random, and there's no common way to have the fields ordered by appearence in the class they're in. Other @JSoapField parameters that can be relevant are **fieldName** (if you want the request field to have a different name than the one in the class field), and a **namespace**. If you add the **@JSoapClass** annotation providing the namespace on top of your class, then this namespace will be used on all fields except those that have the namespace on their @JSoapField annotation.

For those cases when you want to add an attribute, you'll just need to add the **@JSoapAttribute** annotation to the desired field. In this case you may also provide an alternative name.

The following class...

```java
JSoapClass(namespace = "http://tempuri.org/")
public class MyInput extends SOAPSerializable {

    @JSoapReqField(order = 0)
    private String theUsername;
    
    @JSoapReqField(order = 1)
    private String thePassword;
    
    @JSoapAttribute(name="myAttribute")
    private String attribute;
    
}

```
... or the changed version of itself ...

```java
public class MyInput extends SOAPSerializable {

    @JSoapReqField(namespace = "http://tempuri.org/", order = 0, fieldName="theUsername")
    private String user;
    
    @JSoapReqField(namespace = "http://tempuri.org/", fieldName = "thePassword", order = 1)
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

The response class is the class that will handle the response from the webservice call. Let's say that we know the reply will be something like this:

```xml

<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:ws="some-namespace">
   <soap:Body>
      <MyReplyResponse xmlns="http://ws.cdyne.com/WeatherWS/">
         <MyReply myattr="attr">
            <SomeField> <!-- something --> </SomeField>
            <SomeField2> <!-- something --> </SomeField2>
         </MyReply>
      </MyReplyResponse>
   </soap:Body>
</soap:Envelope>            
```

Then you'll just need to create a class extending **SOAPDeserializable**, specify the fields that are attributes (**@JSoapAttribute** annotation) and those which are fields with **@JSoapResField** annotation. The previous response can be mapped like this:

```java

@JSoapClass(namespace = "some-namespace")
public class MyReply extends SOAPDeserializable {

    @JSoapResField(name = "SomeField")
    private String field1;

    @JSoapResField
    private String SomeField2;
}
```
Note again if you ommit the name property of the JSoapResField, the field name will be used to match the reply.


###Making the request

The request is very simple to make. Just call the **get** method from the **SOAPManager** class with the necessary information. Like this:

```java
private static void getWS() {
        String url="your-url/your-service.asmx or .svc";
        String namespace="yournamespace";
        String method = "webservice_method_name";
        String soap_action = "yournamespace/webservice_method_name";

        SOAPManager.get(namespace, url, method, soap_action, new MyInput("username", "password"), MyReply.class, new JSoapCallback() {

            @Override
            public void onSuccess(Object result) {
                MyReply res = (MyReply)result;
                /** do whatever you want to do */
            }

            @Override
            public void onError(int error) {
                switch (error) {
                    case JsoapError.NETWORK_ERROR: Log.v(TAG, "Network error"); break;
                    case JsoapError.PARSE_ERROR: Log.v(TAG, "Parsing error"); break;
                    default: Log.v(TAG, "Unknown error"); break;
                }
            }
        });

    }

```


## Add JSoap to your project
Since JSoap is on Maven Central, adding it to your project is as simple as adding one of the following snippets for Maven or Gradle projects.

### Gradle
```
compile 'com.github.joaocruz04:jsoap:1.0.0'
```

### Maven
```
<dependency>
  <groupId>com.github.joaocruz04</groupId>
  <artifactId>jsoap</artifactId>
  <version>1.0.0</version>
  <type>aar</type>
</dependency>
```

## Changelog

### 1.0.1 (17 Jan 2015)
- As requested, now you can get the raw (String) result from a request by passing **null** as the output class in the SOAPManager.get method.


## License

```
Copyright 2015 João Cruz

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```
