package co.jinear.core.oauth;

import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.entity.oauth.OauthAuthorizationCode;
import co.jinear.core.model.entity.oauth.OauthAuthorizationRequest;
import co.jinear.core.repository.oauth.OauthAuthorizationCodeRepository;
import co.jinear.core.service.oauth.provider.OauthAuthorizationCodeService;
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
class OauthAuthorizationCodeServiceTest {

    private OauthAuthorizationCodeRepository repository;
    private OauthAuthorizationCodeService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(OauthAuthorizationCodeRepository.class);
        OauthProperties properties = new OauthProperties();
        properties.setAuthorizationCodeValiditySeconds(60);
        // Cost 4 keeps the suite fast; production uses the encoder's default.
        service = new OauthAuthorizationCodeService(repository, properties, new BCryptPasswordEncoder(4));
    }

    @Test
    void issuesACodeThatCarriesTheRowIdAndASecret() {
        Mockito.when(repository.save(Mockito.any())).thenAnswer(invocation -> {
            OauthAuthorizationCode saved = invocation.getArgument(0);
            saved.setOauthAuthorizationCodeId("code-row-1");
            return saved;
        });

        String code = service.issue(pendingRequest(), "account-1", "connection-1");

        assertThat(code).startsWith("code-row-1.");
        ArgumentCaptor<OauthAuthorizationCode> captor = ArgumentCaptor.forClass(OauthAuthorizationCode.class);
        Mockito.verify(repository).save(captor.capture());
        // Only the hash is persisted, exactly as the robot token does it.
        assertThat(captor.getValue().getHashedCode()).doesNotContain(code.substring(code.indexOf('.') + 1));
        assertThat(captor.getValue().getCodeChallenge()).isEqualTo("challenge-value");
    }

    @Test
    void redeemsAValidCodeOnce() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        OauthAuthorizationCode stored = storedCode(encoder.encode("secret"), null);
        Mockito.when(repository.findByOauthAuthorizationCodeIdAndPassiveIdIsNull("row-1"))
                .thenReturn(Optional.of(stored));
        service = new OauthAuthorizationCodeService(repository, propertiesWithSixtySeconds(), encoder);

        OauthAuthorizationCode redeemed = service.redeem("row-1.secret");

        assertThat(redeemed.getConsumedAt()).isNotNull();
        Mockito.verify(repository).save(stored);
    }

    @Test
    void refusesAReplayedCode() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        OauthAuthorizationCode stored = storedCode(encoder.encode("secret"), DateHelper.now());
        Mockito.when(repository.findByOauthAuthorizationCodeIdAndPassiveIdIsNull("row-1"))
                .thenReturn(Optional.of(stored));
        service = new OauthAuthorizationCodeService(repository, propertiesWithSixtySeconds(), encoder);

        assertThatThrownBy(() -> service.redeem("row-1.secret"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("oauth.error.invalid-grant");
    }

    @Test
    void refusesAWrongSecretEvenWithTheRightRowId() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        OauthAuthorizationCode stored = storedCode(encoder.encode("secret"), null);
        Mockito.when(repository.findByOauthAuthorizationCodeIdAndPassiveIdIsNull("row-1"))
                .thenReturn(Optional.of(stored));
        service = new OauthAuthorizationCodeService(repository, propertiesWithSixtySeconds(), encoder);

        assertThatThrownBy(() -> service.redeem("row-1.wrong"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("oauth.error.invalid-grant");
    }

    @Test
    void refusesAnExpiredCode() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        OauthAuthorizationCode stored = storedCode(encoder.encode("secret"), null);
        stored.setExpiresAt(DateHelper.substractSeconds(DateHelper.now(), 5));
        Mockito.when(repository.findByOauthAuthorizationCodeIdAndPassiveIdIsNull("row-1"))
                .thenReturn(Optional.of(stored));
        service = new OauthAuthorizationCodeService(repository, propertiesWithSixtySeconds(), encoder);

        assertThatThrownBy(() -> service.redeem("row-1.secret"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("oauth.error.invalid-grant");
    }

    @Test
    void refusesAMalformedCode() {
        assertThatThrownBy(() -> service.redeem("no-separator"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("oauth.error.invalid-grant");
        assertThatThrownBy(() -> service.redeem(null))
                .isInstanceOf(BusinessException.class);
    }

    private OauthProperties propertiesWithSixtySeconds() {
        OauthProperties properties = new OauthProperties();
        properties.setAuthorizationCodeValiditySeconds(60);
        return properties;
    }

    private OauthAuthorizationRequest pendingRequest() {
        OauthAuthorizationRequest request = new OauthAuthorizationRequest();
        request.setClientId("https://claude.ai/client.json");
        request.setRedirectUri("https://claude.ai/api/mcp/auth_callback");
        request.setScope("tasks:read tasks:write");
        request.setCodeChallenge("challenge-value");
        request.setCodeChallengeMethod("S256");
        request.setResource("https://api.jinear.co/mcp");
        return request;
    }

    private OauthAuthorizationCode storedCode(String hashed, java.util.Date consumedAt) {
        OauthAuthorizationCode code = new OauthAuthorizationCode();
        code.setOauthAuthorizationCodeId("row-1");
        code.setHashedCode(hashed);
        code.setAccountId("account-1");
        code.setClientId("https://claude.ai/client.json");
        code.setOauthConnectionId("connection-1");
        code.setRedirectUri("https://claude.ai/api/mcp/auth_callback");
        code.setScope("tasks:read");
        code.setCodeChallenge("challenge-value");
        code.setCodeChallengeMethod("S256");
        code.setExpiresAt(DateHelper.addSeconds(DateHelper.now(), 60));
        code.setConsumedAt(consumedAt);
        return code;
    }
}
