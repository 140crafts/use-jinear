package co.jinear.core.model.response.oauth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The RFC 8414 authorization server metadata document.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"issuer", "authorization_endpoint", "token_endpoint", "revocation_endpoint",
        "registration_endpoint", "scopes_supported", "response_types_supported",
        "grant_types_supported", "token_endpoint_auth_methods_supported",
        "code_challenge_methods_supported", "client_id_metadata_document_supported",
        "service_documentation"})
public class OauthServerMetadataResponse {

    @JsonProperty("issuer")
    private String issuer;

    @JsonProperty("authorization_endpoint")
    private String authorizationEndpoint;

    @JsonProperty("token_endpoint")
    private String tokenEndpoint;

    @JsonProperty("revocation_endpoint")
    private String revocationEndpoint;

    @JsonProperty("registration_endpoint")
    private String registrationEndpoint;

    @JsonProperty("scopes_supported")
    private List<String> scopesSupported;

    @JsonProperty("response_types_supported")
    private List<String> responseTypesSupported;

    @JsonProperty("grant_types_supported")
    private List<String> grantTypesSupported;

    @JsonProperty("token_endpoint_auth_methods_supported")
    private List<String> tokenEndpointAuthMethodsSupported;

    @JsonProperty("code_challenge_methods_supported")
    private List<String> codeChallengeMethodsSupported;

    @JsonProperty("client_id_metadata_document_supported")
    private Boolean clientIdMetadataDocumentSupported;

    @JsonProperty("service_documentation")
    private String serviceDocumentation;
}
