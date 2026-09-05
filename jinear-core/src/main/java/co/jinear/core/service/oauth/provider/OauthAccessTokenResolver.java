package co.jinear.core.service.oauth.provider;

import co.jinear.core.model.entity.oauth.OauthConnection;
import co.jinear.core.model.vo.oauth.OauthAccessTokenVo;
import co.jinear.core.service.oauth.provider.OauthConnectionService;
import co.jinear.core.system.oauth.OauthTokenHelper;
import co.jinear.core.system.util.DateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthAccessTokenResolver {

    private static final String BEARER_PREFIX = "Bearer ";

    private final OauthTokenHelper oauthTokenHelper;
    private final OauthConnectionService oauthConnectionService;

    public Optional<String> extractBearer(String authorizationHeader) {
        if (Objects.isNull(authorizationHeader) || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            return Optional.empty();
        }
        String token = authorizationHeader.substring(BEARER_PREFIX.length()).trim();
        return token.isEmpty() ? Optional.empty() : Optional.of(token);
    }

    private void touchIfStale(OauthConnection connection) {
        Date lastUsedAt = connection.getLastUsedAt();
        if (Objects.isNull(lastUsedAt) || lastUsedAt.before(DateHelper.substractMinutes(DateHelper.now(), 5))) {
            oauthConnectionService.touch(connection);
        }
    }

    public Optional<OauthAccessTokenVo> resolve(String token) {
        Optional<OauthAccessTokenVo> parsed = oauthTokenHelper.parseAccessToken(token);
        if (parsed.isEmpty()) {
            return Optional.empty();
        }
        OauthAccessTokenVo vo = parsed.get();
        Optional<OauthConnection> connection = oauthConnectionService.retrieveOptional(vo.getConnectionId());
        if (connection.isEmpty()) {
            log.info("[OAUTH] Token presented for a revoked or unknown connection: {}", vo.getConnectionId());
            return Optional.empty();
        }
        vo.setSessionInfoId(connection.get().getSessionInfoId());
        touchIfStale(connection.get());
        return Optional.of(vo);
    }
}
