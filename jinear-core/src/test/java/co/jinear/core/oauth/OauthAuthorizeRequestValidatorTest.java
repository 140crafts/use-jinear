package co.jinear.core.oauth;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.vo.oauth.OauthAuthorizeRequestVo;
import co.jinear.core.model.vo.oauth.OauthClientMetadataVo;
import co.jinear.core.model.vo.oauth.OauthErrorVo;
import co.jinear.core.service.oauth.provider.OauthClientService;
import co.jinear.core.service.oauth.provider.PkceValidator;
import co.jinear.core.service.oauth.provider.RedirectUriMatcher;
import co.jinear.core.validator.oauth.OauthAuthorizeRequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OauthAuthorizeRequestValidatorTest {

    private OauthClientService clientService;
    private RedirectUriMatcher redirectUriMatcher;
    private OauthAuthorizeRequestValidator validator;

    @BeforeEach
    void setUp() {
        clientService = Mockito.mock(OauthClientService.class);
        redirectUriMatcher = Mockito.mock(RedirectUriMatcher.class);

        Mockito.lenient().when(clientService.resolveForAuthorization(Mockito.any()))
                .thenReturn(new OauthClientMetadataVo());
        Mockito.lenient().when(clientService.redirectUrisOf(Mockito.any()))
                .thenReturn(List.of("https://claude.test/callback"));

        McpProperties mcpProperties = new McpProperties();
        mcpProperties.setResourceUrl("https://api.jinear.test/mcp");

        validator = new OauthAuthorizeRequestValidator(clientService, redirectUriMatcher, new PkceValidator(), mcpProperties);
    }

    private OauthAuthorizeRequestVo.OauthAuthorizeRequestVoBuilder usable() {
        return OauthAuthorizeRequestVo.builder()
                .responseType("code")
                .clientId("https://claude.test/client")
                .redirectUri("https://claude.test/callback")
                .codeChallenge("a-challenge")
                .codeChallengeMethod("S256")
                .resource("https://api.jinear.test/mcp");
    }

    @Test
    void throwsRatherThanRedirectingWhenTheRedirectUriIsNotRegistered() {
        Mockito.when(redirectUriMatcher.matchesAny(Mockito.any(), Mockito.any())).thenReturn(false);

        assertThatThrownBy(() -> validator.validateClientAndRedirectUri(usable().build()))
                .isInstanceOf(BusinessException.class)
                .hasMessage("oauth.error.invalid-redirect-uri");
    }

    @Test
    void acceptsARegisteredRedirectUri() {
        Mockito.when(redirectUriMatcher.matchesAny(Mockito.any(), Mockito.any())).thenReturn(true);

        validator.validateClientAndRedirectUri(usable().build());
    }

    @Test
    void refusesAnythingButTheAuthorizationCodeFlow() {
        Optional<OauthErrorVo> error = validator.validateRequestParameters(usable().responseType("token").build());

        assertThat(error).isPresent();
        assertThat(error.get().getError()).isEqualTo("unsupported_response_type");
    }

    @Test
    void requiresAPkceChallenge() {
        assertThat(validator.validateRequestParameters(usable().codeChallenge(null).build()))
                .get().extracting(OauthErrorVo::getError).isEqualTo("invalid_request");
        assertThat(validator.validateRequestParameters(usable().codeChallenge("  ").build()))
                .get().extracting(OauthErrorVo::getError).isEqualTo("invalid_request");
    }

    @Test
    void refusesThePlainChallengeMethod() {
        Optional<OauthErrorVo> error = validator.validateRequestParameters(usable().codeChallengeMethod("plain").build());

        assertThat(error).isPresent();
        assertThat(error.get().getError()).isEqualTo("invalid_request");
    }

    @Test
    void refusesATokenRequestedForAnotherResource() {
        Optional<OauthErrorVo> error = validator.validateRequestParameters(
                usable().resource("https://someone-else.test/mcp").build());

        assertThat(error).isPresent();
        assertThat(error.get().getError()).isEqualTo("invalid_target");
    }

    @Test
    void acceptsAnAbsentResourceAndIgnoresTrailingSlashAndCase() {
        assertThat(validator.validateRequestParameters(usable().resource(null).build())).isEmpty();
        assertThat(validator.validateRequestParameters(usable().resource("").build())).isEmpty();
        assertThat(validator.validateRequestParameters(usable().resource("https://API.jinear.test/mcp/").build())).isEmpty();
    }

    @Test
    void passesAUsableRequest() {
        assertThat(validator.validateRequestParameters(usable().build())).isEmpty();
    }
}
