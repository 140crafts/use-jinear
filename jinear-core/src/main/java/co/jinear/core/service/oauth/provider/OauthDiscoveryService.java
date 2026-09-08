package co.jinear.core.service.oauth.provider;

import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.response.oauth.OauthServerMetadataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OauthDiscoveryService {

    private final OauthProperties oauthProperties;

    public OauthServerMetadataResponse authorizationServerMetadata() {
        String issuer = oauthProperties.getIssuerUrl();
        OauthServerMetadataResponse metadata = new OauthServerMetadataResponse();
        metadata.setIssuer(issuer);
        metadata.setAuthorizationEndpoint(issuer + "/v1/oauth/authorize");
        metadata.setTokenEndpoint(issuer + "/v1/oauth/token");
        metadata.setRevocationEndpoint(issuer + "/v1/oauth/revoke");
        if (Boolean.TRUE.equals(oauthProperties.getDcrEnabled())) {
            metadata.setRegistrationEndpoint(issuer + "/v1/oauth/register");
        }
        metadata.setScopesSupported(List.copyOf(OauthScope.allValues()));
        metadata.setResponseTypesSupported(List.of("code"));
        metadata.setGrantTypesSupported(List.of("authorization_code", "refresh_token"));
        metadata.setTokenEndpointAuthMethodsSupported(List.of("none"));
        metadata.setCodeChallengeMethodsSupported(List.of("S256"));
        metadata.setClientIdMetadataDocumentSupported(Boolean.TRUE);
        metadata.setServiceDocumentation(oauthProperties.getDocumentationUrl());
        return metadata;
    }
}
