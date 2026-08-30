package co.jinear.core.model.dto.mcp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * What the consent screen shows about a pending authorization.
 * <p>
 * clientDisplayHost is the host of the client_id URL, not the self asserted
 * client_name, because a Client ID Metadata Document is written by whoever hosts it.
 */
@Getter
@Setter
@ToString
public class McpConsentInfoDto {

    private String requestId;
    private String clientDisplayHost;
    private String clientName;
    private String clientUri;
    private String logoUri;
    private String policyUri;
    private String tosUri;
    private String redirectHost;
    /** True when every redirect the client registered is a loopback address. */
    private Boolean loopbackOnly;
    private List<String> requestedScopes;
}
