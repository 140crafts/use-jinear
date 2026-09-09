package co.jinear.core.model.mcp.schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface McpField {

    int UNSET = Integer.MIN_VALUE;

    String value();

    String hint() default "";

    int minimum() default UNSET;

    int maximum() default UNSET;
}
