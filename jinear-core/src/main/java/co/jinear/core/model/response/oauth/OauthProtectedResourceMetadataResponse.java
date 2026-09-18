package co.jinear.core.model.response.oauth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The RFC 9728 protected resource metadata document.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"resource", "authorization_servers", "scopes_supported",
        "bearer_methods_supported", "resource_documentation"})
public class OauthProtectedResourceMetadataResponse {

    @JsonProperty("resource")
    private String resource;

    @JsonProperty("authorization_servers")
    private List<String> authorizationServers;

    @JsonProperty("scopes_supported")
    private List<String> scopesSupported;

    @JsonProperty("bearer_methods_supported")
    private List<String> bearerMethodsSupported;

    @JsonProperty("resource_documentation")
    private String resourceDocumentation;
}
