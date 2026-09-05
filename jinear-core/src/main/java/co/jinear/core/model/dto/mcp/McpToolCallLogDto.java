package co.jinear.core.model.dto.mcp;

import co.jinear.core.model.enumtype.mcp.McpToolCallStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class McpToolCallLogDto {

    private String mcpToolCallLogId;
    private String oauthConnectionId;
    private String accountId;
    private String workspaceId;
    private String clientId;
    private String toolName;
    private McpToolCallStatus callStatus;
    private String errorCode;
    private Long durationMs;
    private Integer responseBytes;
    private Date createdDate;
}
