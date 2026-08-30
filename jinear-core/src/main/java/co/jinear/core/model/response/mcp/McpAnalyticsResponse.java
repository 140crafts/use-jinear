package co.jinear.core.model.response.mcp;

import co.jinear.core.model.dto.mcp.McpAnalyticsDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpAnalyticsResponse extends BaseResponse {

    @JsonProperty("data")
    private McpAnalyticsDto mcpAnalyticsDto;
}
