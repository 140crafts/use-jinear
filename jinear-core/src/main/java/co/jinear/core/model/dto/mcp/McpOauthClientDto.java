package co.jinear.core.model.dto.mcp;

import co.jinear.core.model.enumtype.mcp.McpClientRegistrationType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class McpOauthClientDto {

    private String clientId;
    private String clientName;
    private String clientUri;
    private String logoUri;
    private List<String> redirectUris;
    private McpClientRegistrationType registrationType;
    private Date clientIdIssuedAt;
}
