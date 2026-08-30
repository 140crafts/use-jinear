package co.jinear.core.model.vo.mcp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class McpClientMetadataVo {

    private String clientId;
    private String clientName;
    private String clientUri;
    private String logoUri;
    private String policyUri;
    private String tosUri;
    private List<String> redirectUris;
    private List<String> grantTypes;
    private String tokenEndpointAuthMethod;
    private String softwareId;
    private String softwareVersion;
}
