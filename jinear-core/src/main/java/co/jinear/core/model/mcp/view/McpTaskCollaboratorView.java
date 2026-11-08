package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpTaskCollaboratorView implements McpToolPayload {

    @McpField("Account id of the collaborator.")
    private String accountId;

    @McpField("Collaborator handle.")
    private String username;
}
