package pt.joaocruz04.lib.misc;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Objects;

import pt.joaocruz04.lib.annotations.JSoapAttribute;
import pt.joaocruz04.lib.annotations.JSoapResField;

/**
 * Created by joaocruz04 on 04/02/15.
 */
public class SoapDeserializer {
    
    public static Object fromSoapObject(Object originalObject, Class outputClass, SoapObject object) {
        Field[] fields = outputClass.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(JSoapResField.class)) {
                f.setAccessible(true);
                String name = f.getAnnotation(JSoapResField.class).name();
                if (name.equals("JSOAP_DEFAULT_FIELD_NAME"))
                    name = f.getName();
                setObjectValue(originalObject, f, object, name, false);
            }
            else if (f.isAnnotationPresent(JSoapAttribute.class)){
                f.setAccessible(true);
                String name = f.getAnnotation(JSoapAttribute.class).name();
                setObjectValue(originalObject, f, object, name, true);
            }
        }
        return originalObject;
    }

    private static void setObjectValue(Object originalObjec, Field f, SoapObject object, String name, boolean isAttribute) {
        if (f.getType().equals(String.class)) {
            setStringValue(originalObjec, object, name, f, isAttribute);
        }
        else if (f.getType().equals(Integer.class) || f.getType().equals(int.class)) {
            setIntValue(originalObjec, object, name, f, isAttribute);
        }
        else if (f.getType().equals(Boolean.class) || f.getType().equals(boolean.class)) {
            setBooleanValue(originalObjec, object, name, f, isAttribute);
        }
        else if (f.getType().equals(Double.class) || f.getType().equals(double.class)) {
            setDoubleValue(originalObjec, object, name, f, isAttribute);
        }
        else if (f.getType().equals(Float.class) || f.getType().equals(float.class)) {
            setFloatValue(originalObjec, object, name, f, isAttribute);
        }
        else if (originalObjec.getClass().isAssignableFrom(f.getType())) {
            setObjectValue(originalObjec, object, name, f, isAttribute);
        }
        else if (f.getType().isArray()) {
            if (f.getType().getComponentType().equals(String.class)) {
                setStringArrayValue(originalObjec, object, f);
            }
            else if (f.getType().getComponentType().equals(Integer.class) || f.getType().getComponentType().equals(int.class)) {
                setIntArrayValue(originalObjec, object, f);
            }
            else if (f.getType().getComponentType().equals(Float.class) || f.getType().getComponentType().equals(float.class)) {
                setFloatArrayValue(originalObjec, object, f);
            }
            else if (f.getType().getComponentType().equals(Double.class) || f.getType().getComponentType().equals(double.class)) {
                setDoubleArrayValue(originalObjec, object, f);
            }
            else if (f.getType().getComponentType().equals(Boolean.class) || f.getType().getComponentType().equals(boolean.class)) {
                setBooleanArrayValue(originalObjec, object, f);
            }
            else {
                setObjectArrayValue(originalObjec, object, f);
            }
        }
    }


    private static void setIntArrayValue(Object originalObjec, SoapObject sObj, Field f) {
        try {
            if (sObj!=null) {
                int count = sObj.getPropertyCount();
                if (count>0) {
                    Integer[] array = new Integer[count];
                    for (int i=0; i<count; i++) {
                        array[i] = Integer.valueOf(sObj.getProperty(i).toString());
                    }
                    f.set(originalObjec, array);
                }
                else {
                    f.set(originalObjec, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setBooleanArrayValue(Object originalObjec, SoapObject sObj, Field f) {
        try {
            if (sObj!=null) {
                int count = sObj.getPropertyCount();
                if (count>0) {
                    Boolean[] array = new Boolean[count];
                    for (int i=0; i<count; i++) {
                        array[i] = Boolean.valueOf(sObj.getProperty(i).toString());
                    }
                    f.set(originalObjec, array);
                }
                else {
                    f.set(originalObjec, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void setFloatArrayValue(Object originalObjec, SoapObject sObj, Field f) {
        try {
            if (sObj!=null) {
                int count = sObj.getPropertyCount();
                if (count>0) {
                    Float[] array = new Float[count];
                    for (int i=0; i<count; i++) {
                        array[i] = Float.valueOf(sObj.getProperty(i).toString());
                    }
                    f.set(originalObjec, array);
                }
                else {
                    f.set(originalObjec, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setDoubleArrayValue(Object originalObjec, SoapObject sObj, Field f) {
        try {
            if (sObj!=null) {
                int count = sObj.getPropertyCount();
                if (count>0) {
                    Double[] array = new Double[count];
                    for (int i=0; i<count; i++) {
                        array[i] = Double.valueOf(sObj.getProperty(i).toString());
                    }
                    f.set(originalObjec, array);
                }
                else {
                    f.set(originalObjec, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setStringArrayValue(Object originalObjec, SoapObject sObj, Field f) {
        try {
            if (sObj!=null) {
                int count = sObj.getPropertyCount();
                if (count>0) {
                    String[] array = new String[count];
                    for (int i=0; i<count; i++) {
                        array[i] = sObj.getProperty(i).toString();
                    }
                    f.set(originalObjec, array);
                }
                else {
                    f.set(originalObjec, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setObjectArrayValue(Object originalObjec, SoapObject sObj, Field f) {
        Class cl = f.getType().getComponentType();
        try {
            if (sObj!=null) {
                int count = sObj.getPropertyCount();
                if (count>0) {
                    Object array = Array.newInstance(cl, count);
                    for (int i=0; i<count; i++) {
                        Object o = cl.newInstance();
                        o = fromSoapObject(o,cl,(SoapObject) sObj.getProperty(i));
                        Array.set(array, i, o);

                    }
                    f.set(originalObjec, array);
                }
                else {
                    f.set(originalObjec, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setObjectValue(Object originalObjec, SoapObject object, String name, Field f, boolean isAttribute) {
        Object s = null;
        try {
            SoapObject trObj = isAttribute ?  (SoapObject) object.getAttribute(name) : (SoapObject) object.getProperty(name);
            if (trObj!=null) {
                s = f.getType().newInstance();
                s = fromSoapObject(s, f.getType(), trObj);
                f.set(originalObjec, s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void setStringValue(Object originalObjec, SoapObject object, String name, Field f, boolean isAttribute) {
        try {
            SoapPrimitive trObj = isAttribute ?  (SoapPrimitive) object.getAttribute(name) : (SoapPrimitive) object.getProperty(name);
            if (trObj!=null) {
                String res = trObj.getValue().toString();
                f.set(originalObjec, res);
            }
        } catch (Exception e) {
            if (!(e instanceof ClassCastException))
                e.printStackTrace();
            try {
                f.set(originalObjec, null);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }
    }

    private static void setBooleanValue(Object originalObjec, SoapObject object, String name, Field f, boolean isAttribute) {
        try {
            SoapPrimitive trObj = isAttribute ?  (SoapPrimitive) object.getAttribute(name) : (SoapPrimitive) object.getProperty(name);
            if (trObj!=null) {
                boolean b = Boolean.valueOf(trObj.getValue().toString());
                f.set(originalObjec, b);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                f.set(originalObjec, false);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }
    }

    private static void setDoubleValue(Object originalObjec, SoapObject object, String name, Field f, boolean isAttribute) {
        try {
            SoapPrimitive trObj = isAttribute ?  (SoapPrimitive) object.getAttribute(name) : (SoapPrimitive) object.getProperty(name);
            if (trObj!=null) {
                double b = Double.valueOf(trObj.getValue().toString());
                f.set(originalObjec, b);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                f.set(originalObjec, 0);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }
    }

    private static void setFloatValue(Object originalObjec, SoapObject object, String name, Field f, boolean isAttribute) {
        try {
            SoapPrimitive trObj = isAttribute ?  (SoapPrimitive) object.getAttribute(name) : (SoapPrimitive) object.getProperty(name);
            if (trObj!=null) {
                float b = Float.valueOf(trObj.getValue().toString());
                f.set(originalObjec, b);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                f.set(originalObjec, 0);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }
    }

    private static void setIntValue(Object originalObjec, SoapObject object, String name, Field f, boolean isAttribute) {
        try {
            SoapPrimitive trObj = isAttribute ?  (SoapPrimitive) object.getAttribute(name) : (SoapPrimitive) object.getProperty(name);
            if (trObj!=null) {
                int b = Integer.valueOf(trObj.getValue().toString());
                f.set(originalObjec, b);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                f.set(originalObjec, 0);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }
    }
    
    
}
