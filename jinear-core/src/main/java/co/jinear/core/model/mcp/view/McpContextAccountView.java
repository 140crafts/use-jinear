package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class McpContextAccountView {

    @McpField("Account id. This is the value assignee fields take.")
    private String accountId;

    @McpField("Account handle.")
    private String username;

    @McpField("Preferred language, EN or TR.")
    private String locale;

    @McpField("IANA time zone id, for example Europe/Istanbul or Etc/GMT-3.")
    private String timeZone;

    @McpField("Current time in the account's time zone, ISO 8601 with offset.")
    private String now;
}
