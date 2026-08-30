package co.jinear.core.model.response.mcp;

import co.jinear.core.model.dto.mcp.McpConnectionDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class McpConnectionListingResponse extends BaseResponse {

    @JsonProperty("data")
    private List<McpConnectionDto> mcpConnectionDtoList;
}
