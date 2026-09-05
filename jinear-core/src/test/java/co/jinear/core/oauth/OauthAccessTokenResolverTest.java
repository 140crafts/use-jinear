package co.jinear.core.oauth;

import co.jinear.core.model.entity.oauth.OauthConnection;
import co.jinear.core.model.vo.oauth.OauthAccessTokenVo;
import co.jinear.core.service.oauth.provider.OauthAccessTokenResolver;
import co.jinear.core.service.oauth.provider.OauthConnectionService;
import co.jinear.core.system.oauth.OauthTokenHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class OauthAccessTokenResolverTest {

    private OauthTokenHelper tokenHelper;
    private OauthConnectionService connectionService;
    private OauthAccessTokenResolver resolver;

    @BeforeEach
    void setUp() {
        tokenHelper = Mockito.mock(OauthTokenHelper.class);
        connectionService = Mockito.mock(OauthConnectionService.class);
        resolver = new OauthAccessTokenResolver(tokenHelper, connectionService);
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
        OauthConnection connection = new OauthConnection();
        connection.setOauthConnectionId("connection-1");
        connection.setSessionInfoId("session-1");
        Mockito.when(connectionService.retrieveOptional("connection-1")).thenReturn(Optional.of(connection));

        var resolved = resolver.resolve("token");

        assertThat(resolved).isPresent();
        assertThat(resolved.get().getAccountId()).isEqualTo("account-1");
        assertThat(resolved.get().sessionInfoId()).isEqualTo("session-1");
    }

    @Test
    void refusesATokenWhoseConnectionWasRevoked() {
        Mockito.when(tokenHelper.parseAccessToken("token")).thenReturn(Optional.of(parsedToken()));
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
        OauthConnection connection = new OauthConnection();
        connection.setOauthConnectionId("connection-1");
        connection.setLastUsedAt(new java.util.Date());
        Mockito.when(connectionService.retrieveOptional("connection-1")).thenReturn(Optional.of(connection));

        resolver.resolve("token");

        Mockito.verify(connectionService, Mockito.never()).touch(Mockito.any());
    }

    private OauthAccessTokenVo parsedToken() {
        OauthAccessTokenVo vo = new OauthAccessTokenVo();
        vo.setAccountId("account-1");
        vo.setConnectionId("connection-1");
        vo.setClientId("https://claude.ai/client.json");
        vo.setScopes(Set.of("tasks:read"));
        return vo;
    }
}
