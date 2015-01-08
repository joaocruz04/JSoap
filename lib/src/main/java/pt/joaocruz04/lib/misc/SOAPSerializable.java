package pt.joaocruz04.lib.misc;

import android.util.Log;
import org.ksoap2.serialization.AttributeInfo;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import pt.joaocruz04.lib.annotations.JSoapAttribute;
import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by BEWARE S.A. on 16/12/14.
 */
public abstract class SOAPSerializable extends SoapObject {

    private HashMap<Integer, Field> fields_map = new HashMap<>();
    private ArrayList<Field> attributes_map = new ArrayList<>();


    protected SOAPSerializable() {
        fields_map = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(JSoapField.class)) {
                int order = f.getAnnotation(JSoapField.class).order();
                fields_map.put(order, f);
            }
            else {
                if (f.isAnnotationPresent(JSoapAttribute.class)) {
                    attributes_map.add(f);
                }
            }
        }
        System.out.println("there.");
    }

    @Override
    public int getAttributeCount() {
        return attributes_map.size();
    }

    @Override
    public Object getAttribute(int index) {
        return attributes_map.get(index);
    }

    @Override
    public void getAttributeInfo(int index, AttributeInfo attributeInfo) {

        Field f = attributes_map.get(index);

        if (f.getType().equals(String.class))
            attributeInfo.type = AttributeInfo.STRING_CLASS;
        else
            attributeInfo.type = f.getClass();

        String prpname = f.getAnnotation(JSoapAttribute.class).name();
        if (prpname.equals("JSOAP_DEFAULT_ATTRIBUTE_NAME"))
            attributeInfo.name = f.getName();
        else
            attributeInfo.name = f.getAnnotation(JSoapAttribute.class).name();

        String prpns = f.getAnnotation(JSoapAttribute.class).namespace();
        if (prpns.equals("JSOAP_DEFAULT_ATTRIBUTE_NAMESPACE")) {
            if (this.getClass().getAnnotation(JSoapClass.class) != null) {
                String ns = this.getClass().getAnnotation(JSoapClass.class).namespace();
                if (ns==null)
                    Log.e("JSoap", "Missing namespace in field " + f.getName() + " in class " + this.getClass() + ". Either declare it at the field SoapRequestAttribute annotation or at the class SoapRequestClass annotation");
                else
                    attributeInfo.namespace = ns;
            }
            else {
                Log.e("JSoap", "Missing namespace in field " + f.getName() + " in class " + this.getClass() + ". Either declare it at the field SoapRequestAttribute annotation or at the class SoapRequestClass annotation");
            }
        }

    }

    @Override
    public Object getProperty(int i) {
        try {
            if (fields_map != null) {
                return fields_map.get(i).get(this);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return fields_map.size();
    }

    @Override
    public void setProperty(int i, Object o) {
        if (fields_map!=null) {
            try {
                fields_map.get(i).set(this, o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        if (fields_map != null) {
            Field f = fields_map.get(i);
            if (f.getType().equals(String.class))
                propertyInfo.type = PropertyInfo.STRING_CLASS;
            else
                propertyInfo.type = f.getClass();

            String prpname = f.getAnnotation(JSoapField.class).fieldName();
            if (prpname.equals("JSOAP_DEFAULT_FIELDNAME"))
                propertyInfo.name = f.getName();
            else
                propertyInfo.name = f.getAnnotation(JSoapField.class).fieldName();


            String prpns = f.getAnnotation(JSoapField.class).namespace();
            if (prpns.equals("JSOAP_DEFAULT_NAMESPACE")) {
                if (this.getClass().getAnnotation(JSoapClass.class) != null) {
                    String ns = this.getClass().getAnnotation(JSoapClass.class).namespace();
                    if (ns==null)
                        Log.e("JSoap", "Missing namespace in field " + f.getName() + " in class " + this.getClass() + ". Either declare it at the field SoapRequestElement annotation or at the class SoapRequestClass annotation");
                    else
                        propertyInfo.namespace = ns;
                }
                else {
                    Log.e("JSoap", "Missing namespace in field " + f.getName() + " in class " + this.getClass() + ". Either declare it at the field SoapRequestElement annotation or at the class SoapRequestClass annotation");
                }
            }
            else {
                propertyInfo.namespace = prpns;
            }
        }
    }


}
