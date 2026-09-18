package co.jinear.core.model.response.mcp;

import co.jinear.core.model.dto.mcp.McpServerInfoDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpServerInfoResponse extends BaseResponse {

    @JsonProperty("data")
    private McpServerInfoDto mcpServerInfoDto;
}
