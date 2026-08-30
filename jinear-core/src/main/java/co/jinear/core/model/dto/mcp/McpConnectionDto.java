package co.jinear.core.model.dto.mcp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class McpConnectionDto {

    private String mcpConnectionId;
    private String accountId;
    private String clientId;
    private String clientName;
    private String clientDisplayHost;
    private List<String> grantedScopes;
    private Date createdDate;
    private Date lastUsedAt;
    private Long callCountLast30Days;
}
