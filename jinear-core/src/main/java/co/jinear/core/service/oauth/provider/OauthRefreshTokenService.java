package co.jinear.core.service.oauth.provider;

import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.entity.oauth.OauthRefreshToken;
import co.jinear.core.repository.oauth.OauthRefreshTokenRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.system.RandomHelper;
import co.jinear.core.system.util.DateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Rotating refresh tokens with reuse detection.
 * <p>
 * OAuth 2.1 requires rotation for public clients, and both DCR and CIMD register a
 * client as public. Presenting a token that has already been rotated means a copy
 * leaked, so the whole connection is revoked instead of just refusing that one token.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OauthRefreshTokenService {

    private static final String SEPARATOR = ".";

    private final OauthRefreshTokenRepository oauthRefreshTokenRepository;
    private final OauthConnectionService oauthConnectionService;
    private final OauthProperties oauthProperties;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PassiveService passiveService;

    public String issue(String connectionId) {
        OauthRefreshToken token = new OauthRefreshToken();
        token.setOauthConnectionId(connectionId);
        token.setExpiresAt(DateHelper.addDays(DateHelper.now(), oauthProperties.getRefreshTokenValidityDays()));
        String secret = RandomHelper.generateULID() + RandomHelper.generateULID();
        token.setHashedToken(bCryptPasswordEncoder.encode(secret));
        OauthRefreshToken saved = oauthRefreshTokenRepository.save(token);
        return saved.getOauthRefreshTokenId() + SEPARATOR + secret;
    }

    public OauthRefreshToken redeem(String presentedToken) {
        if (Objects.isNull(presentedToken) || !presentedToken.contains(SEPARATOR)) {
            throw new BusinessException("oauth.error.invalid-grant");
        }
        int separatorIndex = presentedToken.indexOf(SEPARATOR);
        String tokenId = presentedToken.substring(0, separatorIndex);
        String secret = presentedToken.substring(separatorIndex + 1);

        OauthRefreshToken token = oauthRefreshTokenRepository
                .findByOauthRefreshTokenIdAndPassiveIdIsNull(tokenId)
                .orElseThrow(() -> new BusinessException("oauth.error.invalid-grant"));

        if (!bCryptPasswordEncoder.matches(secret, token.getHashedToken())) {
            throw new BusinessException("oauth.error.invalid-grant");
        }
        if (Objects.nonNull(token.getConsumedAt())) {
            log.warn("[OAUTH] Refresh token reuse detected, revoking connection. oauthConnectionId: {}",
                    token.getOauthConnectionId());
            revokeAllForConnection(token.getOauthConnectionId());
            oauthConnectionService.revoke(token.getOauthConnectionId());
            throw new BusinessException("oauth.error.invalid-grant");
        }
        if (token.getExpiresAt().before(DateHelper.now())) {
            throw new BusinessException("oauth.error.invalid-grant");
        }
        return token;
    }

    /** Marks the presented token spent and returns its successor, in one step. */
    public String rotate(OauthRefreshToken current) {
        String replacement = issue(current.getOauthConnectionId());
        String replacementId = replacement.substring(0, replacement.indexOf(SEPARATOR));
        current.setConsumedAt(DateHelper.now());
        current.setRotatedTo(replacementId);
        oauthRefreshTokenRepository.save(current);
        return replacement;
    }

    public void revokeAllForConnection(String connectionId) {
        List<OauthRefreshToken> tokens = oauthRefreshTokenRepository.findAllByOauthConnectionIdAndPassiveIdIsNull(connectionId);
        if (tokens.isEmpty()) {
            return;
        }
        String passiveId = passiveService.createSystemActionPassive();
        tokens.forEach(token -> token.setPassiveId(passiveId));
        oauthRefreshTokenRepository.saveAll(tokens);
    }
}
