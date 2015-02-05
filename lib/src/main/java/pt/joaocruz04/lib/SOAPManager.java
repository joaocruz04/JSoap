package pt.joaocruz04.lib;

import android.os.AsyncTask;
import android.util.Log;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.*;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;
import pt.joaocruz04.lib.annotations.JSoapAttribute;
import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapReqField;
import pt.joaocruz04.lib.misc.JSoapCallback;
import pt.joaocruz04.lib.misc.JsoapError;
import pt.joaocruz04.lib.misc.SoapDeserializer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Joao Cruz on 16/12/14.
 */
public class SOAPManager {

    private static final String TAG = "SOAPManager";



    public static void get(final String namespace, final String url, final String methodName, final String soap_action, final Object obj, final Class outputClass, final JSoapCallback callback) {

        final ArrayList<ComparableProperty> parameters = extractProperties(obj);
        Collections.sort(parameters, new ComparablePropertyComparator());

        (new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                try {
                    SoapObject request = new SoapObject(namespace, methodName);

                    if (parameters != null) for (ComparableProperty propertyInfo : parameters)
                        request.addProperty(propertyInfo.property);
                    //SoapObject reqO = SOAPSerializer.createSoapObject(obj);
                    addAttributes(request, obj);

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.implicitTypes = true;
                    envelope.dotNet = false;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
                    androidHttpTransport.debug = true;
                    androidHttpTransport.call(soap_action, envelope);

                    String reqDump = prettyXML(androidHttpTransport.requestDump);
                    String resDump = prettyXML(androidHttpTransport.responseDump);

                    callback.onDebugMessage("REQUEST " + methodName + ":", reqDump);
                    callback.onDebugMessage("RESPONSE " + methodName + ":", resDump);

                    if (outputClass == null) {
                        callback.onSuccess(resDump);
                        return null;
                    }

                    Object reslt = envelope.getResponse();

                    if (reslt == null) {
                        callback.onSuccess((SoapObject) null);
                        return null;
                    } else {
                        if (reslt.toString().equals("")) {
                            Log.e(TAG, "The " + methodName + " webservice returned empty");
                            callback.onError(JsoapError.OTHER_ERROR);
                            return null;
                        } else if (reslt instanceof SoapObject) {
                            if (outputClass != null) {
                                Object obj = outputClass.newInstance();
                                Object o = SoapDeserializer.fromSoapObject(obj, outputClass, (SoapObject) reslt);
                                callback.onSuccess(o);
                            }
                        } else if (reslt instanceof SoapPrimitive) {

                            if (outputClass != null) {
                                if (outputClass.equals(String.class)) {
                                    String str = ((SoapPrimitive) reslt).getValue().toString();
                                    callback.onSuccess(str);
                                }
                                else if ((outputClass.equals(Integer.class)) || (outputClass.equals(int.class))) {
                                    Integer i = Integer.valueOf(((SoapPrimitive) reslt).getValue().toString());
                                    callback.onSuccess(i);
                                }
                                else if ((outputClass.equals(Double.class)) || (outputClass.equals(double.class))) {
                                    Double i = Double.valueOf(((SoapPrimitive) reslt).getValue().toString());
                                    callback.onSuccess(i);
                                }
                                else if ((outputClass.equals(Float.class)) || (outputClass.equals(float.class))) {
                                    Float i = Float.valueOf(((SoapPrimitive) reslt).getValue().toString());
                                    callback.onSuccess(i);
                                }
                                else if ((outputClass.equals(Boolean.class)) || (outputClass.equals(boolean.class))) {
                                    Boolean i = Boolean.valueOf(((SoapPrimitive) reslt).getValue().toString());
                                    callback.onSuccess(i);
                                }
                            }
                            else {
                                Log.e(TAG, "The " + methodName + " webservice returns a primitive object, but you didn't specify a valid primitive output class.");
                                callback.onError(JsoapError.OTHER_ERROR);
                            }
                        } else {
                            callback.onError(JsoapError.OTHER_ERROR);
                            return null;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onError(JsoapError.NETWORK_ERROR);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                    callback.onError(JsoapError.PARSE_ERROR);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(JsoapError.OTHER_ERROR);
                }

                return null;
            }
        }).execute();
    }


    private static String prettyXML(String src) {
        StringBuilder res = new StringBuilder();

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document d = docBuilder.parse(new InputSource(new StringReader(src)));

            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer  = transFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            DOMSource source = new DOMSource(d);

            transformer.transform(source, result);
            res.append(writer.toString());

        } catch(javax.xml.transform.TransformerException e) {
            e.printStackTrace();
        }catch (java.io.IOException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return res.toString();
    }

    private static void addAttributes(SoapObject request, Object obj) {

        if ((request == null) || (obj == null)) return;
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(JSoapAttribute.class)) {
                f.setAccessible(true);
                String name=null, namespace=null;

                String prpname = f.getAnnotation(JSoapAttribute.class).name();
                if (prpname.equals("JSOAP_DEFAULT_ATTRIBUTE_NAME"))
                    name = f.getName();
                else
                    name = f.getAnnotation(JSoapAttribute.class).name();

                String prpns = f.getAnnotation(JSoapAttribute.class).namespace();
                if (prpns.equals("JSOAP_DEFAULT_ATTRIBUTE_NAMESPACE")) {
                    if (obj.getClass().getAnnotation(JSoapClass.class) != null) {
                        namespace = obj.getClass().getAnnotation(JSoapClass.class).namespace();
                        if (namespace==null)
                            Log.e("JSoap", "Missing namespace in field " + f.getName() + " in class " + obj.getClass() + ". Either declare it at the field SoapRequestAttribute annotation or at the class SoapRequestClass annotation");
                    }
                    else {
                        Log.e("JSoap", "Missing namespace in field " + f.getName() + " in class " + obj.getClass() + ". Either declare it at the field SoapRequestAttribute annotation or at the class SoapRequestClass annotation");
                    }
                }

                AttributeInfo info = new AttributeInfo();
                info.type = obj.getClass();
                info.name = name;
                info.namespace = namespace;
                Object value = null;

                try {
                    value = f.get(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                info.setValue(value);
                request.addAttribute(info);
            }
        }


    }

    private static ArrayList<ComparableProperty> extractProperties(Object obj) {
        ArrayList<ComparableProperty> parameters = new ArrayList<ComparableProperty>();

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(JSoapReqField.class)) {
                f.setAccessible(true);
                String name=null, ns=null;


                String prpname = f.getAnnotation(JSoapReqField.class).fieldName();
                if (prpname.equals("JSOAP_DEFAULT_FIELDNAME"))
                    name = f.getName();
                else
                    name = f.getAnnotation(JSoapReqField.class).fieldName();


                String prpns = f.getAnnotation(JSoapReqField.class).namespace();
                if (prpns.equals("JSOAP_DEFAULT_NAMESPACE")) {
                    if (obj.getClass().getAnnotation(JSoapClass.class) != null) {
                        ns = obj.getClass().getAnnotation(JSoapClass.class).namespace();
                        if (ns == null) Log.e("JSoap", "Missing namespace in field " + f.getName() + " in class " + obj.getClass() + ". Either declare it at the field SoapRequestElement annotation or at the class SoapRequestClass annotation");
                    } else {
                        Log.e("JSoap", "Missing namespace in field " + f.getName() + " in class " + obj.getClass() + ". Either declare it at the field SoapRequestElement annotation or at the class SoapRequestClass annotation");
                    }
                } else {
                    ns = prpns;
                }


                int order = f.getAnnotation(JSoapReqField.class).order();
                try {
                    PropertyInfo info = createPropertyInfo(f.get(obj), ns, name, f.getType());
                    parameters.add(new ComparableProperty(info, order));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return parameters;

    }

    private static PropertyInfo createPropertyInfo(Object object, String namespace, String name, Object type) {
        PropertyInfo localPropertyInfo1 = new PropertyInfo();
        localPropertyInfo1.namespace = namespace;
        localPropertyInfo1.setName(name);
        localPropertyInfo1.setValue(object);
        localPropertyInfo1.setType(type);
        return localPropertyInfo1;
    }







    private static class ComparableProperty {
        public PropertyInfo property;
        public int order;

        private ComparableProperty(PropertyInfo property, int order) {
            this.property = property;
            this.order = order;
        }
    }

    private static class ComparablePropertyComparator implements Comparator<ComparableProperty> {
        @Override
        public int compare(ComparableProperty lhs, ComparableProperty rhs) {
            return lhs.order-rhs.order;
        }
    }


}
