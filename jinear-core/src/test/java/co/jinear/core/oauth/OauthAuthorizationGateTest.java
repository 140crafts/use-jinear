package co.jinear.core.oauth;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.converter.oauth.OauthDtoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.manager.oauth.provider.OauthAuthorizationManager;
import co.jinear.core.model.entity.oauth.OauthAuthorizationRequest;
import co.jinear.core.model.vo.oauth.OauthAuthorizeRequestVo;
import co.jinear.core.model.vo.oauth.OauthErrorVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.oauth.provider.*;
import co.jinear.core.validator.oauth.OauthAuthorizeRequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OauthAuthorizationGateTest {

    private OauthProperties oauthProperties;
    private OauthAuthorizeRequestValidator validator;
    private OauthAuthorizationRequestService requestService;
    private OauthAuthorizationManager manager;

    @BeforeEach
    void setUp() {
        oauthProperties = new OauthProperties();
        validator = Mockito.mock(OauthAuthorizeRequestValidator.class);
        requestService = Mockito.mock(OauthAuthorizationRequestService.class);

        FeProperties feProperties = new FeProperties();
        feProperties.setOauthConsentUrl("https://jinear.test/oauth/consent?request_id={requestId}");

        manager = new OauthAuthorizationManager(
                validator,
                Mockito.mock(OauthClientService.class),
                new OauthScopeService(),
                requestService,
                Mockito.mock(OauthAuthorizationCodeService.class),
                Mockito.mock(OauthConnectionService.class),
                Mockito.mock(OauthDtoConverter.class),
                Mockito.mock(SessionInfoService.class),
                oauthProperties,
                feProperties);
    }

    private OauthAuthorizeRequestVo request() {
        return OauthAuthorizeRequestVo.builder()
                .responseType("code")
                .clientId("https://claude.test/client")
                .redirectUri("https://claude.test/callback")
                .scope("tasks:read")
                .state("state-1")
                .codeChallenge("a-challenge")
                .codeChallengeMethod("S256")
                .resource("https://api.jinear.test/mcp")
                .build();
    }

    @Test
    void refusesAuthorizeWhenTheAuthorizationServerIsOff() {
        oauthProperties.setEnabled(Boolean.FALSE);

        assertThatThrownBy(() -> manager.authorize(request()))
                .isInstanceOf(BusinessException.class)
                .hasMessage("oauth.error.disabled");
        Mockito.verifyNoInteractions(validator);
        Mockito.verifyNoInteractions(requestService);
    }

    @Test
    void refusesConsentReadWhenTheAuthorizationServerIsOff() {
        oauthProperties.setEnabled(Boolean.FALSE);

        assertThatThrownBy(() -> manager.retrieveConsentInfo("req-1"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("oauth.error.disabled");
        Mockito.verifyNoInteractions(requestService);
    }

    @Test
    void sendsTheUserToTheConsentScreenWhenTheRequestIsUsable() {
        oauthProperties.setEnabled(Boolean.TRUE);
        Mockito.when(requestService.initialize(Mockito.any(), Mockito.any()))
                .thenAnswer(invocation -> {
                    OauthAuthorizationRequest parked = new OauthAuthorizationRequest();
                    parked.setOauthAuthorizationRequestId("req-1");
                    return parked;
                });

        assertThat(manager.authorize(request()))
                .isEqualTo("https://jinear.test/oauth/consent?request_id=req-1");
    }

    @Test
    void bouncesAValidatorErrorBackToTheClientAsARedirect() {
        oauthProperties.setEnabled(Boolean.TRUE);
        Mockito.when(validator.validateRequestParameters(Mockito.any()))
                .thenReturn(Optional.of(new OauthErrorVo("invalid_target", "Wrong resource.")));

        assertThat(manager.authorize(request()))
                .startsWith("https://claude.test/callback?")
                .contains("error=invalid_target")
                .contains("error_description=Wrong+resource.")
                .contains("state=state-1");
        Mockito.verifyNoInteractions(requestService);
    }
}
