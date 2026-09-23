package co.jinear.core.oauth;

import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.model.response.oauth.OauthServerMetadataResponse;
import co.jinear.core.service.oauth.provider.OauthDiscoveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OauthDiscoveryServiceTest {

    private OauthProperties properties;
    private OauthDiscoveryService service;

    @BeforeEach
    void setUp() {
        properties = new OauthProperties();
        properties.setIssuerUrl("https://api.jinear.test");
        properties.setDocumentationUrl("https://jinear.co/mcp/");
        properties.setDcrEnabled(Boolean.TRUE);
        service = new OauthDiscoveryService(properties);
    }

    @Test
    void authorizationServerMetadataAdvertisesS256() {
        assertThat(service.authorizationServerMetadata().getCodeChallengeMethodsSupported())
                .isEqualTo(List.of("S256"));
    }

    @Test
    void authorizationServerMetadataEnablesClientIdMetadataDocuments() {
        OauthServerMetadataResponse document = service.authorizationServerMetadata();

        assertThat(document.getClientIdMetadataDocumentSupported()).isEqualTo(Boolean.TRUE);
        assertThat(document.getTokenEndpointAuthMethodsSupported()).isEqualTo(List.of("none"));
    }

    @Test
    void authorizationServerMetadataNamesEveryEndpoint() {
        OauthServerMetadataResponse document = service.authorizationServerMetadata();

        assertThat(document.getIssuer()).isEqualTo("https://api.jinear.test");
        assertThat(document.getAuthorizationEndpoint()).isEqualTo("https://api.jinear.test/v1/oauth/authorize");
        assertThat(document.getTokenEndpoint()).isEqualTo("https://api.jinear.test/v1/oauth/token");
        assertThat(document.getRegistrationEndpoint()).isEqualTo("https://api.jinear.test/v1/oauth/register");
        assertThat(document.getRevocationEndpoint()).isEqualTo("https://api.jinear.test/v1/oauth/revoke");
        assertThat(document.getGrantTypesSupported()).isEqualTo(List.of("authorization_code", "refresh_token"));
        assertThat(document.getResponseTypesSupported()).isEqualTo(List.of("code"));
    }

    @Test
    void omitsTheRegistrationEndpointWhenDynamicRegistrationIsTurnedOff() {
        properties.setDcrEnabled(Boolean.FALSE);

        assertThat(service.authorizationServerMetadata().getRegistrationEndpoint()).isNull();
    }

    @Test
    void advertisesEveryScopeTheEnumDefines() {
        assertThat(service.authorizationServerMetadata().getScopesSupported())
                .contains("workspace:read", "tasks:read", "tasks:write", "offline_access");
    }
}
