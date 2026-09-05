package co.jinear.core.service.oauth.provider;

import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.entity.oauth.OauthConnection;
import co.jinear.core.model.enumtype.auth.ProviderType;
import co.jinear.core.repository.oauth.OauthConnectionRepository;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.system.util.DateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthConnectionService {

    private final OauthConnectionRepository oauthConnectionRepository;
    private final OauthScopeService oauthScopeService;
    private final SessionInfoService sessionInfoService;
    private final PassiveService passiveService;

    public OauthConnection grant(String accountId, String clientId, String clientName, Set<String> scopes) {
        OauthConnection connection = oauthConnectionRepository
                .findFirstByAccountIdAndClientIdAndPassiveIdIsNull(accountId, clientId)
                .orElseGet(OauthConnection::new);
        connection.setAccountId(accountId);
        connection.setClientId(clientId);
        connection.setClientName(clientName);
        connection.setGrantedScopes(oauthScopeService.format(scopes));
        if (Objects.isNull(connection.getSessionInfoId())) {
            connection.setSessionInfoId(sessionInfoService.initialize(ProviderType.OAUTH_CONNECTION, accountId));
        }
        OauthConnection saved = oauthConnectionRepository.save(connection);
        log.info("[OAUTH] Granted connection. accountId: {}, clientId: {}, scopes: {}", accountId, clientId, scopes);
        return saved;
    }

    public Optional<OauthConnection> retrieveOptional(String oauthConnectionId) {
        return oauthConnectionRepository.findByOauthConnectionIdAndPassiveIdIsNull(oauthConnectionId);
    }

    public OauthConnection retrieve(String oauthConnectionId) {
        return retrieveOptional(oauthConnectionId).orElseThrow(NotFoundException::new);
    }

    public List<OauthConnection> listForAccount(String accountId) {
        return oauthConnectionRepository.findAllByAccountIdAndPassiveIdIsNullOrderByCreatedDateDesc(accountId);
    }

    public long countActive() {
        return oauthConnectionRepository.countByPassiveIdIsNull();
    }

    public void touch(OauthConnection connection) {
        connection.setLastUsedAt(DateHelper.now());
        oauthConnectionRepository.save(connection);
    }

    public String revoke(String oauthConnectionId) {
        OauthConnection connection = retrieve(oauthConnectionId);
        String passiveId = passiveService.createUserActionPassive(connection.getAccountId());
        connection.setPassiveId(passiveId);
        oauthConnectionRepository.save(connection);
        log.info("[OAUTH] Revoked connection. oauthConnectionId: {}", oauthConnectionId);
        return passiveId;
    }
}
