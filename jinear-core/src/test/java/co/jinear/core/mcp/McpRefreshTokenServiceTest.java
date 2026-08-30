package co.jinear.core.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.entity.mcp.McpRefreshToken;
import co.jinear.core.repository.mcp.McpRefreshTokenRepository;
import co.jinear.core.service.mcp.oauth.McpConnectionService;
import co.jinear.core.service.mcp.oauth.McpRefreshTokenService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.system.util.DateHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * OAuth 2.1 requires refresh token rotation for public clients, and every MCP client is
 * a public client. Rotation without reuse detection is only half the protection: a
 * stolen token is only harmless if presenting a spent one ends the grant.
 */
class McpRefreshTokenServiceTest {

    private McpRefreshTokenRepository repository;
    private McpConnectionService connectionService;
    private McpRefreshTokenService service;
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(McpRefreshTokenRepository.class);
        connectionService = Mockito.mock(McpConnectionService.class);
        PassiveService passiveService = Mockito.mock(PassiveService.class);
        Mockito.when(passiveService.createSystemActionPassive()).thenReturn("passive-1");
        encoder = new BCryptPasswordEncoder(4);

        McpProperties properties = new McpProperties();
        properties.setRefreshTokenValidityDays(30);
        service = new McpRefreshTokenService(repository, connectionService, properties, encoder, passiveService);
    }

    @Test
    void issuesATokenCarryingTheRowIdAndASecret() {
        Mockito.when(repository.save(Mockito.any())).thenAnswer(invocation -> {
            McpRefreshToken saved = invocation.getArgument(0);
            saved.setMcpRefreshTokenId("token-row-1");
            return saved;
        });

        String token = service.issue("connection-1");

        assertThat(token).startsWith("token-row-1.");
    }

    @Test
    void redeemsALiveToken() {
        McpRefreshToken stored = storedToken(encoder.encode("secret"), null);
        Mockito.when(repository.findByMcpRefreshTokenIdAndPassiveIdIsNull("row-1")).thenReturn(Optional.of(stored));

        assertThat(service.redeem("row-1.secret")).isSameAs(stored);
    }

    @Test
    void rotationMarksTheOldTokenSpentAndPointsItAtItsSuccessor() {
        McpRefreshToken current = storedToken(encoder.encode("secret"), null);
        Mockito.when(repository.save(Mockito.any())).thenAnswer(invocation -> {
            McpRefreshToken saved = invocation.getArgument(0);
            if (saved.getMcpRefreshTokenId() == null) {
                saved.setMcpRefreshTokenId("row-2");
            }
            return saved;
        });

        String replacement = service.rotate(current);

        assertThat(replacement).startsWith("row-2.");
        assertThat(current.getConsumedAt()).isNotNull();
        assertThat(current.getRotatedTo()).isEqualTo("row-2");
    }

    @Test
    void reusingASpentTokenRevokesTheWholeConnection() {
        McpRefreshToken spent = storedToken(encoder.encode("secret"), DateHelper.now());
        spent.setRotatedTo("row-2");
        Mockito.when(repository.findByMcpRefreshTokenIdAndPassiveIdIsNull("row-1")).thenReturn(Optional.of(spent));
        Mockito.when(repository.findAllByMcpConnectionIdAndPassiveIdIsNull("connection-1")).thenReturn(List.of(spent));

        assertThatThrownBy(() -> service.redeem("row-1.secret"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("mcp.error.oauth.invalid-grant");

        Mockito.verify(connectionService).revoke("connection-1");
    }

    @Test
    void refusesAWrongSecret() {
        McpRefreshToken stored = storedToken(encoder.encode("secret"), null);
        Mockito.when(repository.findByMcpRefreshTokenIdAndPassiveIdIsNull("row-1")).thenReturn(Optional.of(stored));

        assertThatThrownBy(() -> service.redeem("row-1.wrong"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("mcp.error.oauth.invalid-grant");
        // A wrong secret is a guess, not a leak, so the connection survives.
        Mockito.verify(connectionService, Mockito.never()).revoke(Mockito.anyString());
    }

    @Test
    void refusesAnExpiredToken() {
        McpRefreshToken stored = storedToken(encoder.encode("secret"), null);
        stored.setExpiresAt(DateHelper.substractDays(DateHelper.now(), 1));
        Mockito.when(repository.findByMcpRefreshTokenIdAndPassiveIdIsNull("row-1")).thenReturn(Optional.of(stored));

        assertThatThrownBy(() -> service.redeem("row-1.secret"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("mcp.error.oauth.invalid-grant");
    }

    @Test
    void refusesAnUnknownToken() {
        Mockito.when(repository.findByMcpRefreshTokenIdAndPassiveIdIsNull("row-9")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.redeem("row-9.secret"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("mcp.error.oauth.invalid-grant");
    }

    private McpRefreshToken storedToken(String hashed, java.util.Date consumedAt) {
        McpRefreshToken token = new McpRefreshToken();
        token.setMcpRefreshTokenId("row-1");
        token.setHashedToken(hashed);
        token.setMcpConnectionId("connection-1");
        token.setExpiresAt(DateHelper.addDays(DateHelper.now(), 30));
        token.setConsumedAt(consumedAt);
        return token;
    }
}
