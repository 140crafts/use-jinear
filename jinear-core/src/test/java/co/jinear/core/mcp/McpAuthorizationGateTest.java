package co.jinear.core.mcp;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.manager.mcp.McpAuthorizationManager;
import co.jinear.core.model.enumtype.management.InstanceFlagType;
import co.jinear.core.model.vo.mcp.McpAuthorizeRequestVo;
import co.jinear.core.model.vo.mcp.McpClientMetadataVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.management.InstanceFlagService;
import co.jinear.core.service.mcp.oauth.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * The two switches in front of the authorization endpoint.
 * <p>
 * The instance flag is checked here and not on the transport, so turning it off stops
 * anybody granting fresh access while the connections people already made keep working.
 * That is the behaviour the admin screen promises, and it only holds if this refusal
 * happens before a request is parked.
 */
class McpAuthorizationGateTest {

    private McpProperties properties;
    private InstanceFlagService instanceFlagService;
    private McpAuthorizationRequestService requestService;
    private McpAuthorizationManager manager;

    @BeforeEach
    void setUp() {
        properties = new McpProperties();
        properties.setResourceUrl("https://api.jinear.test/mcp");
        properties.setIssuerUrl("https://api.jinear.test");

        instanceFlagService = Mockito.mock(InstanceFlagService.class);
        requestService = Mockito.mock(McpAuthorizationRequestService.class);

        McpOauthClientService clientService = Mockito.mock(McpOauthClientService.class);
        McpClientMetadataVo client = new McpClientMetadataVo();
        Mockito.lenient().when(clientService.resolveForAuthorization(Mockito.any())).thenReturn(client);
        Mockito.lenient().when(clientService.redirectUrisOf(Mockito.any()))
                .thenReturn(List.of("https://claude.test/callback"));

        McpRedirectUriMatcher redirectUriMatcher = Mockito.mock(McpRedirectUriMatcher.class);
        Mockito.lenient().when(redirectUriMatcher.matchesAny(Mockito.any(), Mockito.any())).thenReturn(true);

        FeProperties feProperties = new FeProperties();
        feProperties.setMcpConsentUrl("https://jinear.test/oauth/consent?request_id={requestId}");

        manager = new McpAuthorizationManager(
                clientService,
                redirectUriMatcher,
                new McpPkceValidator(),
                new McpScopeService(),
                requestService,
                Mockito.mock(McpAuthorizationCodeService.class),
                Mockito.mock(McpConnectionService.class),
                Mockito.mock(SessionInfoService.class),
                properties,
                feProperties,
                instanceFlagService);
    }

    private McpAuthorizeRequestVo request() {
        return McpAuthorizeRequestVo.builder()
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
        properties.setEnabled(Boolean.TRUE);
        Mockito.when(instanceFlagService.isEnabled(InstanceFlagType.MCP_SERVER)).thenReturn(false);

        assertThatThrownBy(() -> manager.authorize(request()))
                .isInstanceOf(BusinessException.class)
                .hasMessage("mcp.error.disabled");
        Mockito.verifyNoInteractions(requestService);
    }

    @Test
    void refusesWhenTheServerIsNotConfigured() {
        properties.setEnabled(Boolean.FALSE);
        Mockito.lenient().when(instanceFlagService.isEnabled(InstanceFlagType.MCP_SERVER)).thenReturn(true);

        assertThatThrownBy(() -> manager.authorize(request()))
                .isInstanceOf(BusinessException.class)
                .hasMessage("mcp.error.disabled");
        Mockito.verifyNoInteractions(requestService);
    }

    /**
     * The happy path is here only to prove the two refusals above are caused by the
     * switches and not by the rest of the request being unusable.
     */
    @Test
    void sendsTheUserToTheConsentScreenWhenBothSwitchesAgree() {
        properties.setEnabled(Boolean.TRUE);
        Mockito.when(instanceFlagService.isEnabled(InstanceFlagType.MCP_SERVER)).thenReturn(true);
        Mockito.when(requestService.initialize(Mockito.any(), Mockito.any(), Mockito.any(),
                        Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenAnswer(invocation -> {
                    var parked = new co.jinear.core.model.entity.mcp.McpAuthorizationRequest();
                    parked.setMcpAuthorizationRequestId("req-1");
                    return parked;
                });

        assertThat(manager.authorize(request()))
                .isEqualTo("https://jinear.test/oauth/consent?request_id=req-1");
    }
}
