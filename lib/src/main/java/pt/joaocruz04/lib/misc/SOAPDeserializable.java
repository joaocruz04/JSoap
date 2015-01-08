package pt.joaocruz04.lib.misc;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import pt.joaocruz04.lib.annotations.JSoapAttribute;
import pt.joaocruz04.lib.annotations.JSoapResField;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * Created by Joao Cruz on 16/12/14.
 */
public abstract class SOAPDeserializable {


    public void fromSOAPObject(SoapObject object) {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(JSoapResField.class)) {
                f.setAccessible(true);
                String name = f.getAnnotation(JSoapResField.class).name();
                if (name.equals("JSOAP_DEFAULT_FIELD_NAME"))
                    name = f.getName();
                setObjectValue(f, object, name, false);
            }
            else if (f.isAnnotationPresent(JSoapAttribute.class)){
                f.setAccessible(true);
                String name = f.getAnnotation(JSoapAttribute.class).name();
                setObjectValue(f, object, name, true);
            }
        }
    }

    private void setObjectValue(Field f, SoapObject object, String name, boolean isAttribute) {
        if (f.getType().equals(String.class)) {
            setStringValue(object, name, f, isAttribute);
        }
        else if (f.getType().equals(Integer.class) || f.getType().equals(int.class)) {
            setIntValue(object, name, f, isAttribute);
        }
        else if (f.getType().equals(Boolean.class) || f.getType().equals(boolean.class)) {
            setBooleanValue(object, name, f, isAttribute);
        }
        else if (f.getType().equals(Double.class) || f.getType().equals(double.class)) {
            setDoubleValue(object, name, f, isAttribute);
        }
        else if (f.getType().equals(Float.class) || f.getType().equals(float.class)) {
            setFloatValue(object, name, f, isAttribute);
        }
        else if (SOAPDeserializable.class.isAssignableFrom(f.getType())) {
            setObjectValue(object, name, f, isAttribute);
        }
        else if (f.getType().isArray()) {
            if (f.getType().getComponentType().equals(String.class)) {
                setStringArrayValue(object, f);
            }
            else if (f.getType().getComponentType().equals(Integer.class) || f.getType().getComponentType().equals(int.class)) {
                setIntArrayValue(object, f);
            }
            else if (SOAPDeserializable.class.isAssignableFrom(f.getType().getComponentType())){
                setObjectArrayValue(object, f);
            }
            else if (f.getType().getComponentType().equals(Float.class) || f.getType().getComponentType().equals(float.class)) {
                setFloatArrayValue(object, f);
            }
            else if (f.getType().getComponentType().equals(Double.class) || f.getType().getComponentType().equals(double.class)) {
                setDoubleArrayValue(object, f);
            }
            else if (f.getType().getComponentType().equals(Boolean.class) || f.getType().getComponentType().equals(boolean.class)) {
                setBooleanArrayValue(object, f);
            }
        }
    }


    private void setIntArrayValue(SoapObject sObj, Field f) {
        try {
            if (sObj!=null) {
                int count = sObj.getPropertyCount();
                if (count>0) {
                    Integer[] array = new Integer[count];
                    for (int i=0; i<count; i++) {
                        array[i] = Integer.valueOf(sObj.getProperty(i).toString());
                    }
                    f.set(this, array);
                }
                else {
                    f.set(this, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBooleanArrayValue(SoapObject sObj, Field f) {
        try {
            if (sObj!=null) {
                int count = sObj.getPropertyCount();
                if (count>0) {
                    Boolean[] array = new Boolean[count];
                    for (int i=0; i<count; i++) {
                        array[i] = Boolean.valueOf(sObj.getProperty(i).toString());
                    }
                    f.set(this, array);
                }
                else {
                    f.set(this, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setFloatArrayValue(SoapObject sObj, Field f) {
        try {
            if (sObj!=null) {
                int count = sObj.getPropertyCount();
                if (count>0) {
                    Float[] array = new Float[count];
                    for (int i=0; i<count; i++) {
                        array[i] = Float.valueOf(sObj.getProperty(i).toString());
                    }
                    f.set(this, array);
                }
                else {
                    f.set(this, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDoubleArrayValue(SoapObject sObj, Field f) {
        try {
            if (sObj!=null) {
                int count = sObj.getPropertyCount();
                if (count>0) {
                    Double[] array = new Double[count];
                    for (int i=0; i<count; i++) {
                        array[i] = Double.valueOf(sObj.getProperty(i).toString());
                    }
                    f.set(this, array);
                }
                else {
                    f.set(this, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setStringArrayValue(SoapObject sObj, Field f) {
        try {
            if (sObj!=null) {
                int count = sObj.getPropertyCount();
                if (count>0) {
                    String[] array = new String[count];
                    for (int i=0; i<count; i++) {
                        array[i] = sObj.getProperty(i).toString();
                    }
                    f.set(this, array);
                }
                else {
                    f.set(this, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setObjectArrayValue(SoapObject sObj, Field f) {
        Class cl = f.getType().getComponentType();
        try {
            if (sObj!=null) {
                int count = sObj.getPropertyCount();
                if (count>0) {
                    Object array = Array.newInstance(cl, count);
                    for (int i=0; i<count; i++) {
                        SOAPDeserializable obj = (SOAPDeserializable) cl.newInstance();
                        obj.fromSOAPObject((SoapObject) sObj.getProperty(i));
                        Array.set(array, i, obj);

                    }
                    f.set(this, array);
                }
                else {
                    f.set(this, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setObjectValue(SoapObject object, String name, Field f, boolean isAttribute) {
        SOAPDeserializable s = null;
        try {
            SoapObject trObj = isAttribute ?  (SoapObject) object.getAttribute(name) : (SoapObject) object.getProperty(name);
            if (trObj!=null) {
                s = (SOAPDeserializable) f.getType().newInstance();
                s.fromSOAPObject(trObj);
                f.set(this, s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setStringValue(SoapObject object, String name, Field f, boolean isAttribute) {
        try {
            SoapPrimitive trObj = isAttribute ?  (SoapPrimitive) object.getAttribute(name) : (SoapPrimitive) object.getProperty(name);
            if (trObj!=null) {
                String res = trObj.getValue().toString();
                f.set(this, res);
            }
        } catch (Exception e) {
            if (!(e instanceof ClassCastException))
                e.printStackTrace();
            try {
                f.set(this, null);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void setBooleanValue(SoapObject object, String name, Field f, boolean isAttribute) {
        try {
            SoapPrimitive trObj = isAttribute ?  (SoapPrimitive) object.getAttribute(name) : (SoapPrimitive) object.getProperty(name);
            if (trObj!=null) {
                boolean b = Boolean.valueOf(trObj.getValue().toString());
                f.set(this, b);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                f.set(this, false);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void setDoubleValue(SoapObject object, String name, Field f, boolean isAttribute) {
        try {
            SoapPrimitive trObj = isAttribute ?  (SoapPrimitive) object.getAttribute(name) : (SoapPrimitive) object.getProperty(name);
            if (trObj!=null) {
                double b = Double.valueOf(trObj.getValue().toString());
                f.set(this, b);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                f.set(this, 0);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void setFloatValue(SoapObject object, String name, Field f, boolean isAttribute) {
        try {
            SoapPrimitive trObj = isAttribute ?  (SoapPrimitive) object.getAttribute(name) : (SoapPrimitive) object.getProperty(name);
            if (trObj!=null) {
                float b = Float.valueOf(trObj.getValue().toString());
                f.set(this, b);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                f.set(this, 0);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void setIntValue(SoapObject object, String name, Field f, boolean isAttribute) {
        try {
            SoapPrimitive trObj = isAttribute ?  (SoapPrimitive) object.getAttribute(name) : (SoapPrimitive) object.getProperty(name);
            if (trObj!=null) {
                int b = Integer.valueOf(trObj.getValue().toString());
                f.set(this, b);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                f.set(this, 0);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }
    }


}
