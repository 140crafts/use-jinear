package co.jinear.core.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.entity.mcp.McpAuthorizationCode;
import co.jinear.core.model.entity.mcp.McpAuthorizationRequest;
import co.jinear.core.repository.mcp.McpAuthorizationCodeRepository;
import co.jinear.core.service.mcp.oauth.McpAuthorizationCodeService;
import co.jinear.core.system.util.DateHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * An authorization code is the one bearer credential that travels through a browser
 * redirect, so single use and short lived are the properties that matter.
 */
class McpAuthorizationCodeServiceTest {

    private McpAuthorizationCodeRepository repository;
    private McpAuthorizationCodeService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(McpAuthorizationCodeRepository.class);
        McpProperties properties = new McpProperties();
        properties.setAuthorizationCodeValiditySeconds(60);
        // Cost 4 keeps the suite fast; production uses the encoder's default.
        service = new McpAuthorizationCodeService(repository, properties, new BCryptPasswordEncoder(4));
    }

    @Test
    void issuesACodeThatCarriesTheRowIdAndASecret() {
        Mockito.when(repository.save(Mockito.any())).thenAnswer(invocation -> {
            McpAuthorizationCode saved = invocation.getArgument(0);
            saved.setMcpAuthorizationCodeId("code-row-1");
            return saved;
        });

        String code = service.issue(pendingRequest(), "account-1", "connection-1");

        assertThat(code).startsWith("code-row-1.");
        ArgumentCaptor<McpAuthorizationCode> captor = ArgumentCaptor.forClass(McpAuthorizationCode.class);
        Mockito.verify(repository).save(captor.capture());
        // Only the hash is persisted, exactly as the robot token does it.
        assertThat(captor.getValue().getHashedCode()).doesNotContain(code.substring(code.indexOf('.') + 1));
        assertThat(captor.getValue().getCodeChallenge()).isEqualTo("challenge-value");
    }

    @Test
    void redeemsAValidCodeOnce() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        McpAuthorizationCode stored = storedCode(encoder.encode("secret"), null);
        Mockito.when(repository.findByMcpAuthorizationCodeIdAndPassiveIdIsNull("row-1"))
                .thenReturn(Optional.of(stored));
        service = new McpAuthorizationCodeService(repository, propertiesWithSixtySeconds(), encoder);

        McpAuthorizationCode redeemed = service.redeem("row-1.secret");

        assertThat(redeemed.getConsumedAt()).isNotNull();
        Mockito.verify(repository).save(stored);
    }

    @Test
    void refusesAReplayedCode() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        McpAuthorizationCode stored = storedCode(encoder.encode("secret"), DateHelper.now());
        Mockito.when(repository.findByMcpAuthorizationCodeIdAndPassiveIdIsNull("row-1"))
                .thenReturn(Optional.of(stored));
        service = new McpAuthorizationCodeService(repository, propertiesWithSixtySeconds(), encoder);

        assertThatThrownBy(() -> service.redeem("row-1.secret"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("mcp.error.oauth.invalid-grant");
    }

    @Test
    void refusesAWrongSecretEvenWithTheRightRowId() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        McpAuthorizationCode stored = storedCode(encoder.encode("secret"), null);
        Mockito.when(repository.findByMcpAuthorizationCodeIdAndPassiveIdIsNull("row-1"))
                .thenReturn(Optional.of(stored));
        service = new McpAuthorizationCodeService(repository, propertiesWithSixtySeconds(), encoder);

        assertThatThrownBy(() -> service.redeem("row-1.wrong"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("mcp.error.oauth.invalid-grant");
    }

    @Test
    void refusesAnExpiredCode() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        McpAuthorizationCode stored = storedCode(encoder.encode("secret"), null);
        stored.setExpiresAt(DateHelper.substractSeconds(DateHelper.now(), 5));
        Mockito.when(repository.findByMcpAuthorizationCodeIdAndPassiveIdIsNull("row-1"))
                .thenReturn(Optional.of(stored));
        service = new McpAuthorizationCodeService(repository, propertiesWithSixtySeconds(), encoder);

        assertThatThrownBy(() -> service.redeem("row-1.secret"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("mcp.error.oauth.invalid-grant");
    }

    @Test
    void refusesAMalformedCode() {
        assertThatThrownBy(() -> service.redeem("no-separator"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("mcp.error.oauth.invalid-grant");
        assertThatThrownBy(() -> service.redeem(null))
                .isInstanceOf(BusinessException.class);
    }

    private McpProperties propertiesWithSixtySeconds() {
        McpProperties properties = new McpProperties();
        properties.setAuthorizationCodeValiditySeconds(60);
        return properties;
    }

    private McpAuthorizationRequest pendingRequest() {
        McpAuthorizationRequest request = new McpAuthorizationRequest();
        request.setClientId("https://claude.ai/client.json");
        request.setRedirectUri("https://claude.ai/api/mcp/auth_callback");
        request.setScope("tasks:read tasks:write");
        request.setCodeChallenge("challenge-value");
        request.setCodeChallengeMethod("S256");
        request.setResource("https://api.jinear.co/mcp");
        return request;
    }

    private McpAuthorizationCode storedCode(String hashed, java.util.Date consumedAt) {
        McpAuthorizationCode code = new McpAuthorizationCode();
        code.setMcpAuthorizationCodeId("row-1");
        code.setHashedCode(hashed);
        code.setAccountId("account-1");
        code.setClientId("https://claude.ai/client.json");
        code.setMcpConnectionId("connection-1");
        code.setRedirectUri("https://claude.ai/api/mcp/auth_callback");
        code.setScope("tasks:read");
        code.setCodeChallenge("challenge-value");
        code.setCodeChallengeMethod("S256");
        code.setExpiresAt(DateHelper.addSeconds(DateHelper.now(), 60));
        code.setConsumedAt(consumedAt);
        return code;
    }
}
