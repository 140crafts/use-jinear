package co.jinear.core.service.oauth.provider;

import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * RFC 8414 Authorization Server Metadata: what a client reads before it can authorize.
 * <p>
 * A plain map rather than a typed response so it serializes exactly as the RFC specifies,
 * and so it stays out of the generated frontend type file.
 */
@Service
@RequiredArgsConstructor
public class OauthDiscoveryService {

    private final OauthProperties oauthProperties;

    /**
     * Two fields decide how Claude registers itself: a client identified by a metadata
     * document is only used when {@code client_id_metadata_document_supported} is true
     * AND {@code "none"} appears in the token endpoint auth methods, because that
     * client authenticates as a public client with PKCE and no secret. Drop either and
     * every connection falls back to dynamic registration.
     * <p>
     * {@code scopes_supported} is one flat {@link OauthScope} set because MCP is the only
     * resource this server protects. A second resource makes the advertised set depend on
     * which resource the client asked about.
     */
    public Map<String, Object> authorizationServerMetadata() {
        String issuer = oauthProperties.getIssuerUrl();
        Map<String, Object> document = new LinkedHashMap<>();
        document.put("issuer", issuer);
        document.put("authorization_endpoint", issuer + "/v1/oauth/authorize");
        document.put("token_endpoint", issuer + "/v1/oauth/token");
        document.put("revocation_endpoint", issuer + "/v1/oauth/revoke");
        if (Boolean.TRUE.equals(oauthProperties.getDcrEnabled())) {
            document.put("registration_endpoint", issuer + "/v1/oauth/register");
        }
        document.put("scopes_supported", List.copyOf(OauthScope.allValues()));
        document.put("response_types_supported", List.of("code"));
        document.put("grant_types_supported", List.of("authorization_code", "refresh_token"));
        document.put("token_endpoint_auth_methods_supported", List.of("none"));
        document.put("code_challenge_methods_supported", List.of("S256"));
        document.put("client_id_metadata_document_supported", Boolean.TRUE);
        document.put("service_documentation", oauthProperties.getDocumentationUrl());
        return document;
    }
}
