package co.jinear.core.service.oauth.provider;

import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OauthDiscoveryService {

    private final OauthProperties oauthProperties;

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
