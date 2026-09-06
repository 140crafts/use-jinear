package co.jinear.core.oauth;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.manager.oauth.provider.OauthAuthorizationManager;
import co.jinear.core.model.enumtype.management.InstanceFlagType;
import co.jinear.core.model.vo.oauth.OauthAuthorizeRequestVo;
import co.jinear.core.model.vo.oauth.OauthClientMetadataVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.management.InstanceFlagService;
import co.jinear.core.service.oauth.provider.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OauthAuthorizationGateTest {

    private OauthProperties oauthProperties;
    private InstanceFlagService instanceFlagService;
    private OauthAuthorizationRequestService requestService;
    private OauthAuthorizationManager manager;

    @BeforeEach
    void setUp() {
        oauthProperties = new OauthProperties();
        McpProperties mcpProperties = new McpProperties();
        mcpProperties.setResourceUrl("https://api.jinear.test/mcp");

        instanceFlagService = Mockito.mock(InstanceFlagService.class);
        requestService = Mockito.mock(OauthAuthorizationRequestService.class);

        OauthClientService clientService = Mockito.mock(OauthClientService.class);
        OauthClientMetadataVo client = new OauthClientMetadataVo();
        Mockito.lenient().when(clientService.resolveForAuthorization(Mockito.any())).thenReturn(client);
        Mockito.lenient().when(clientService.redirectUrisOf(Mockito.any()))
                .thenReturn(List.of("https://claude.test/callback"));

        RedirectUriMatcher redirectUriMatcher = Mockito.mock(RedirectUriMatcher.class);
        Mockito.lenient().when(redirectUriMatcher.matchesAny(Mockito.any(), Mockito.any())).thenReturn(true);

        FeProperties feProperties = new FeProperties();
        feProperties.setOauthConsentUrl("https://jinear.test/oauth/consent?request_id={requestId}");

        manager = new OauthAuthorizationManager(
                clientService,
                redirectUriMatcher,
                new PkceValidator(),
                new OauthScopeService(),
                requestService,
                Mockito.mock(OauthAuthorizationCodeService.class),
                Mockito.mock(OauthConnectionService.class),
                Mockito.mock(SessionInfoService.class),
                oauthProperties,
                mcpProperties,
                feProperties,
                instanceFlagService);
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
    void refusesWhenTheAdministratorHasTurnedTheFlagOff() {
        oauthProperties.setEnabled(Boolean.TRUE);
        Mockito.when(instanceFlagService.isEnabled(InstanceFlagType.MCP_SERVER)).thenReturn(false);

        assertThatThrownBy(() -> manager.authorize(request()))
                .isInstanceOf(BusinessException.class)
                .hasMessage("oauth.error.disabled");
        Mockito.verifyNoInteractions(requestService);
    }

    @Test
    void refusesWhenTheServerIsNotConfigured() {
        oauthProperties.setEnabled(Boolean.FALSE);
        Mockito.lenient().when(instanceFlagService.isEnabled(InstanceFlagType.MCP_SERVER)).thenReturn(true);

        assertThatThrownBy(() -> manager.authorize(request()))
                .isInstanceOf(BusinessException.class)
                .hasMessage("oauth.error.disabled");
        Mockito.verifyNoInteractions(requestService);
    }

    @Test
    void sendsTheUserToTheConsentScreenWhenBothSwitchesAgree() {
        oauthProperties.setEnabled(Boolean.TRUE);
        Mockito.when(instanceFlagService.isEnabled(InstanceFlagType.MCP_SERVER)).thenReturn(true);
        Mockito.when(requestService.initialize(Mockito.any(), Mockito.any(), Mockito.any(),
                        Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenAnswer(invocation -> {
                    var parked = new co.jinear.core.model.entity.oauth.OauthAuthorizationRequest();
                    parked.setOauthAuthorizationRequestId("req-1");
                    return parked;
                });

        assertThat(manager.authorize(request()))
                .isEqualTo("https://jinear.test/oauth/consent?request_id=req-1");
    }
}
