package co.jinear.core.service.oauth.provider;

import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.entity.oauth.OauthAuthorizationCode;
import co.jinear.core.model.entity.oauth.OauthAuthorizationRequest;
import co.jinear.core.repository.oauth.OauthAuthorizationCodeRepository;
import co.jinear.core.system.RandomHelper;
import co.jinear.core.system.util.DateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthAuthorizationCodeService {

    private static final String SEPARATOR = ".";

    private final OauthAuthorizationCodeRepository oauthAuthorizationCodeRepository;
    private final OauthProperties oauthProperties;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String issue(OauthAuthorizationRequest request, String accountId, String connectionId) {
        OauthAuthorizationCode code = new OauthAuthorizationCode();
        code.setAccountId(accountId);
        code.setClientId(request.getClientId());
        code.setOauthConnectionId(connectionId);
        code.setRedirectUri(request.getRedirectUri());
        code.setScope(request.getScope());
        code.setCodeChallenge(request.getCodeChallenge());
        code.setCodeChallengeMethod(request.getCodeChallengeMethod());
        code.setResource(request.getResource());
        code.setExpiresAt(DateHelper.addSeconds(DateHelper.now(), oauthProperties.getAuthorizationCodeValiditySeconds()));

        String secret = RandomHelper.generateULID() + RandomHelper.generateULID();
        code.setHashedCode(bCryptPasswordEncoder.encode(secret));
        OauthAuthorizationCode saved = oauthAuthorizationCodeRepository.save(code);
        return saved.getOauthAuthorizationCodeId() + SEPARATOR + secret;
    }

    public OauthAuthorizationCode redeem(String presentedCode) {
        if (Objects.isNull(presentedCode) || !presentedCode.contains(SEPARATOR)) {
            throw new BusinessException("oauth.error.invalid-grant");
        }
        int separatorIndex = presentedCode.indexOf(SEPARATOR);
        String codeId = presentedCode.substring(0, separatorIndex);
        String secret = presentedCode.substring(separatorIndex + 1);

        OauthAuthorizationCode code = oauthAuthorizationCodeRepository
                .findByOauthAuthorizationCodeIdAndPassiveIdIsNull(codeId)
                .orElseThrow(() -> new BusinessException("oauth.error.invalid-grant"));

        if (Objects.nonNull(code.getConsumedAt())) {
            log.warn("[OAUTH] Authorization code replayed. codeId: {}", codeId);
            throw new BusinessException("oauth.error.invalid-grant");
        }
        if (code.getExpiresAt().before(DateHelper.now())) {
            throw new BusinessException("oauth.error.invalid-grant");
        }
        if (!bCryptPasswordEncoder.matches(secret, code.getHashedCode())) {
            throw new BusinessException("oauth.error.invalid-grant");
        }

        code.setConsumedAt(DateHelper.now());
        oauthAuthorizationCodeRepository.save(code);
        return code;
    }

    public int purgeExpiredBefore(Date before) {
        return oauthAuthorizationCodeRepository.deleteAllExpiredBefore(before);
    }
}
