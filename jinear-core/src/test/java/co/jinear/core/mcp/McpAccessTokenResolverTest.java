package co.jinear.core.mcp;

import co.jinear.core.model.entity.mcp.McpConnection;
import co.jinear.core.model.vo.mcp.McpAccessTokenVo;
import co.jinear.core.service.mcp.McpAccessTokenResolver;
import co.jinear.core.service.mcp.oauth.McpConnectionService;
import co.jinear.core.system.mcp.McpTokenHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A valid signature is not enough on its own.
 * <p>
 * Access tokens live for an hour, so if the only check were the signature, revoking a
 * connection would leave it working for up to an hour afterwards. Checking the connection
 * on every call is what makes the revoke button in the settings screen mean something.
 */
class McpAccessTokenResolverTest {

    private McpTokenHelper tokenHelper;
    private McpConnectionService connectionService;
    private McpAccessTokenResolver resolver;

    @BeforeEach
    void setUp() {
        tokenHelper = Mockito.mock(McpTokenHelper.class);
        connectionService = Mockito.mock(McpConnectionService.class);
        resolver = new McpAccessTokenResolver(tokenHelper, connectionService);
    }

    @Test
    void readsABearerHeader() {
        assertThat(resolver.extractBearer("Bearer abc")).contains("abc");
    }

    @Test
    void ignoresAnythingThatIsNotABearerHeader() {
        assertThat(resolver.extractBearer(null)).isEmpty();
        assertThat(resolver.extractBearer("Basic abc")).isEmpty();
        assertThat(resolver.extractBearer("Bearer ")).isEmpty();
    }

    @Test
    void resolvesALiveConnectionAndCarriesItsSessionId() {
        Mockito.when(tokenHelper.parseAccessToken("token")).thenReturn(Optional.of(parsedToken()));
        McpConnection connection = new McpConnection();
        connection.setMcpConnectionId("connection-1");
        connection.setSessionInfoId("session-1");
        Mockito.when(connectionService.retrieveOptional("connection-1")).thenReturn(Optional.of(connection));

        var resolved = resolver.resolve("token");

        assertThat(resolved).isPresent();
        assertThat(resolved.get().getAccountId()).isEqualTo("account-1");
        // Write managers stamp this onto every activity row they emit.
        assertThat(resolved.get().sessionInfoId()).isEqualTo("session-1");
    }

    @Test
    void refusesATokenWhoseConnectionWasRevoked() {
        Mockito.when(tokenHelper.parseAccessToken("token")).thenReturn(Optional.of(parsedToken()));
        // A revoked connection is soft deleted, so the lookup that filters on passive_id
        // finds nothing.
        Mockito.when(connectionService.retrieveOptional("connection-1")).thenReturn(Optional.empty());

        assertThat(resolver.resolve("token")).isEmpty();
    }

    @Test
    void refusesATokenTheHelperWouldNotParse() {
        Mockito.when(tokenHelper.parseAccessToken("token")).thenReturn(Optional.empty());

        assertThat(resolver.resolve("token")).isEmpty();
        Mockito.verifyNoInteractions(connectionService);
    }

    @Test
    void refreshesLastUsedOnlyWhenItIsStale() {
        Mockito.when(tokenHelper.parseAccessToken("token")).thenReturn(Optional.of(parsedToken()));
        McpConnection connection = new McpConnection();
        connection.setMcpConnectionId("connection-1");
        connection.setLastUsedAt(new java.util.Date());
        Mockito.when(connectionService.retrieveOptional("connection-1")).thenReturn(Optional.of(connection));

        resolver.resolve("token");

        // Just used, so no write in front of the read.
        Mockito.verify(connectionService, Mockito.never()).touch(Mockito.any());
    }

    private McpAccessTokenVo parsedToken() {
        McpAccessTokenVo vo = new McpAccessTokenVo();
        vo.setAccountId("account-1");
        vo.setConnectionId("connection-1");
        vo.setClientId("https://claude.ai/client.json");
        vo.setScopes(Set.of("tasks:read"));
        return vo;
    }
}
