package co.jinear.core.model.dto.mcp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class McpServerInfoDto {

    private Boolean enabled;
    private String serverUrl;
    private String documentationUrl;
}
