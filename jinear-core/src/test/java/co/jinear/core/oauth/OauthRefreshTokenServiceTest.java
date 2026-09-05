package co.jinear.core.oauth;

import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.entity.oauth.OauthRefreshToken;
import co.jinear.core.repository.oauth.OauthRefreshTokenRepository;
import co.jinear.core.service.oauth.provider.OauthConnectionService;
import co.jinear.core.service.oauth.provider.OauthRefreshTokenService;
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

class OauthRefreshTokenServiceTest {

    private OauthRefreshTokenRepository repository;
    private OauthConnectionService connectionService;
    private OauthRefreshTokenService service;
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(OauthRefreshTokenRepository.class);
        connectionService = Mockito.mock(OauthConnectionService.class);
        PassiveService passiveService = Mockito.mock(PassiveService.class);
        Mockito.when(passiveService.createSystemActionPassive()).thenReturn("passive-1");
        encoder = new BCryptPasswordEncoder(4);

        OauthProperties properties = new OauthProperties();
        properties.setRefreshTokenValidityDays(30);
        service = new OauthRefreshTokenService(repository, connectionService, properties, encoder, passiveService);
    }

    @Test
    void issuesATokenCarryingTheRowIdAndASecret() {
        Mockito.when(repository.save(Mockito.any())).thenAnswer(invocation -> {
            OauthRefreshToken saved = invocation.getArgument(0);
            saved.setOauthRefreshTokenId("token-row-1");
            return saved;
        });

        String token = service.issue("connection-1");

        assertThat(token).startsWith("token-row-1.");
    }

    @Test
    void redeemsALiveToken() {
        OauthRefreshToken stored = storedToken(encoder.encode("secret"), null);
        Mockito.when(repository.findByOauthRefreshTokenIdAndPassiveIdIsNull("row-1")).thenReturn(Optional.of(stored));

        assertThat(service.redeem("row-1.secret")).isSameAs(stored);
    }

    @Test
    void rotationMarksTheOldTokenSpentAndPointsItAtItsSuccessor() {
        OauthRefreshToken current = storedToken(encoder.encode("secret"), null);
        Mockito.when(repository.save(Mockito.any())).thenAnswer(invocation -> {
            OauthRefreshToken saved = invocation.getArgument(0);
            if (saved.getOauthRefreshTokenId() == null) {
                saved.setOauthRefreshTokenId("row-2");
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
        OauthRefreshToken spent = storedToken(encoder.encode("secret"), DateHelper.now());
        spent.setRotatedTo("row-2");
        Mockito.when(repository.findByOauthRefreshTokenIdAndPassiveIdIsNull("row-1")).thenReturn(Optional.of(spent));
        Mockito.when(repository.findAllByOauthConnectionIdAndPassiveIdIsNull("connection-1")).thenReturn(List.of(spent));

        assertThatThrownBy(() -> service.redeem("row-1.secret"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("oauth.error.invalid-grant");

        Mockito.verify(connectionService).revoke("connection-1");
    }

    @Test
    void refusesAWrongSecret() {
        OauthRefreshToken stored = storedToken(encoder.encode("secret"), null);
        Mockito.when(repository.findByOauthRefreshTokenIdAndPassiveIdIsNull("row-1")).thenReturn(Optional.of(stored));

        assertThatThrownBy(() -> service.redeem("row-1.wrong"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("oauth.error.invalid-grant");
        Mockito.verify(connectionService, Mockito.never()).revoke(Mockito.anyString());
    }

    @Test
    void refusesAnExpiredToken() {
        OauthRefreshToken stored = storedToken(encoder.encode("secret"), null);
        stored.setExpiresAt(DateHelper.substractDays(DateHelper.now(), 1));
        Mockito.when(repository.findByOauthRefreshTokenIdAndPassiveIdIsNull("row-1")).thenReturn(Optional.of(stored));

        assertThatThrownBy(() -> service.redeem("row-1.secret"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("oauth.error.invalid-grant");
    }

    @Test
    void refusesAnUnknownToken() {
        Mockito.when(repository.findByOauthRefreshTokenIdAndPassiveIdIsNull("row-9")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.redeem("row-9.secret"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("oauth.error.invalid-grant");
    }

    private OauthRefreshToken storedToken(String hashed, java.util.Date consumedAt) {
        OauthRefreshToken token = new OauthRefreshToken();
        token.setOauthRefreshTokenId("row-1");
        token.setHashedToken(hashed);
        token.setOauthConnectionId("connection-1");
        token.setExpiresAt(DateHelper.addDays(DateHelper.now(), 30));
        token.setConsumedAt(consumedAt);
        return token;
    }
}
