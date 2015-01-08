package pt.joaocruz04.lib.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by João Cruz on 16/12/14.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JSoapResField {
    String name() default "JSOAP_DEFAULT_FIELD_NAME";
}
