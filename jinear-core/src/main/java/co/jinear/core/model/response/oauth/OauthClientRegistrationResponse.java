package co.jinear.core.model.response.oauth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The RFC 7591 dynamic client registration response.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"client_id", "client_id_issued_at", "client_name", "redirect_uris",
        "grant_types", "response_types", "token_endpoint_auth_method"})
public class OauthClientRegistrationResponse {

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("client_id_issued_at")
    private Long clientIdIssuedAt;

    @JsonProperty("client_name")
    private String clientName;

    @JsonProperty("redirect_uris")
    private List<String> redirectUris;

    @JsonProperty("grant_types")
    private List<String> grantTypes;

    @JsonProperty("response_types")
    private List<String> responseTypes;

    @JsonProperty("token_endpoint_auth_method")
    private String tokenEndpointAuthMethod;
}
