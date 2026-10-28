package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class McpContextMemberView {

    @McpField("Account id. This is the value assignee fields take.")
    private String accountId;

    @McpField("Account handle.")
    private String username;
}
