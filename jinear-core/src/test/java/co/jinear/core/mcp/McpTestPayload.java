package co.jinear.core.mcp;

import co.jinear.core.model.mcp.schema.McpField;
import co.jinear.core.model.mcp.view.McpToolPayload;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * A payload for the protocol tests, standing in for a real tool's view.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpTestPayload implements McpToolPayload {

    @McpField("Account the call ran as.")
    private String accountId;

    @McpField("What the stub tool answered.")
    private String result;

    static McpTestPayload result(String result) {
        McpTestPayload payload = new McpTestPayload();
        payload.setResult(result);
        return payload;
    }

    static McpTestPayload forAccount(String accountId) {
        McpTestPayload payload = new McpTestPayload();
        payload.setAccountId(accountId);
        return payload;
    }
}
