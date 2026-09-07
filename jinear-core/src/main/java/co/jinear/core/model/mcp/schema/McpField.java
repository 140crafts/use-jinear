package co.jinear.core.model.mcp.schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Carries the JSON Schema description of a payload field, so the schema an MCP client reads
 * is generated from the class it describes instead of being written a second time by hand.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface McpField {

    String value();
}
