package co.jinear.core.model.response.mcp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * The RFC 6750 error body an MCP auth challenge answers with. It does not extend
 * {@code BaseResponse}: the shape is fixed by the specification.
 */
@Getter
@Setter
@AllArgsConstructor
@JsonPropertyOrder({"error", "error_description"})
public class McpProtocolErrorResponse {

    @JsonProperty("error")
    private String error;

    @JsonProperty("error_description")
    private String errorDescription;
}
