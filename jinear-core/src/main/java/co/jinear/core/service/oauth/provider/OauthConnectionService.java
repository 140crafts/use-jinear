package co.jinear.core.service.oauth.provider;

import co.jinear.core.exception.NotFoundException;
import co.jinear.core.converter.oauth.OauthDtoConverter;
import co.jinear.core.model.dto.oauth.OauthConnectionDto;
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
    private final OauthDtoConverter oauthDtoConverter;

    public OauthConnectionDto grant(String accountId, String clientId, String clientName, Set<String> scopes) {
        OauthConnection connection = retrieve(accountId, clientId);
        connection.setAccountId(accountId);
        connection.setClientId(clientId);
        connection.setClientName(clientName);
        connection.setGrantedScopes(oauthScopeService.format(scopes));
        if (Objects.isNull(connection.getSessionInfoId())) {
            connection.setSessionInfoId(sessionInfoService.initialize(ProviderType.OAUTH_CONNECTION, accountId));
        }
        OauthConnection saved = oauthConnectionRepository.save(connection);
        log.info("[OAUTH] Granted connection. accountId: {}, clientId: {}, scopes: {}", accountId, clientId, scopes);
        return oauthDtoConverter.convert(saved, null);
    }

    public Optional<OauthConnectionDto> retrieveOptional(String oauthConnectionId) {
        return oauthConnectionRepository.findByOauthConnectionIdAndPassiveIdIsNull(oauthConnectionId)
                .map(connection -> oauthDtoConverter.convert(connection, null));
    }

    public OauthConnectionDto retrieve(String oauthConnectionId) {
        return retrieveOptional(oauthConnectionId).orElseThrow(NotFoundException::new);
    }

    public List<OauthConnectionDto> listForAccount(String accountId) {
        return oauthConnectionRepository.findAllByAccountIdAndPassiveIdIsNullOrderByCreatedDateDesc(accountId)
                .stream()
                .map(connection -> oauthDtoConverter.convert(connection, null))
                .toList();
    }

    public long countActive() {
        return oauthConnectionRepository.countByPassiveIdIsNull();
    }

    public void touch(String oauthConnectionId) {
        OauthConnection connection = retrieveEntity(oauthConnectionId);
        connection.setLastUsedAt(DateHelper.now());
        oauthConnectionRepository.save(connection);
    }


    OauthConnection retrieveEntity(String oauthConnectionId) {
        return oauthConnectionRepository.findByOauthConnectionIdAndPassiveIdIsNull(oauthConnectionId)
                .orElseThrow(NotFoundException::new);
    }

    public String revoke(String oauthConnectionId) {
        OauthConnection connection = retrieveEntity(oauthConnectionId);
        String passiveId = passiveService.createUserActionPassive(connection.getAccountId());
        connection.setPassiveId(passiveId);
        oauthConnectionRepository.save(connection);
        log.info("[OAUTH] Revoked connection. oauthConnectionId: {}", oauthConnectionId);
        return passiveId;
    }

    private OauthConnection retrieve(String accountId, String clientId) {
        return oauthConnectionRepository
                .findFirstByAccountIdAndClientIdAndPassiveIdIsNull(accountId, clientId)
                .orElseGet(OauthConnection::new);
    }
}
