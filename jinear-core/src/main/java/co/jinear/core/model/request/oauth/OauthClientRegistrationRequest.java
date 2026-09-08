package co.jinear.core.model.request.oauth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * The RFC 7591 dynamic client registration body. Unknown members are ignored, as the
 * specification requires of a registration endpoint.
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class OauthClientRegistrationRequest {

    @JsonProperty("client_name")
    private String clientName;

    @JsonProperty("client_uri")
    private String clientUri;

    @JsonProperty("logo_uri")
    private String logoUri;

    @JsonProperty("policy_uri")
    private String policyUri;

    @JsonProperty("tos_uri")
    private String tosUri;

    @JsonProperty("redirect_uris")
    private List<String> redirectUris;

    @JsonProperty("grant_types")
    private List<String> grantTypes;

    @JsonProperty("token_endpoint_auth_method")
    private String tokenEndpointAuthMethod;

    @JsonProperty("software_id")
    private String softwareId;

    @JsonProperty("software_version")
    private String softwareVersion;
}
