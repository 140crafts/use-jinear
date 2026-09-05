package co.jinear.core.manager.oauth.provider;

import co.jinear.core.converter.oauth.OauthDtoConverter;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.oauth.OauthConnectionDto;
import co.jinear.core.model.entity.oauth.OauthConnection;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.oauth.OauthConnectionListingResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.oauth.provider.OauthConnectionService;
import co.jinear.core.service.oauth.provider.OauthRefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthConnectionManager {

    private final OauthConnectionService oauthConnectionService;
    private final OauthRefreshTokenService oauthRefreshTokenService;
    private final OauthDtoConverter oauthDtoConverter;
    private final SessionInfoService sessionInfoService;

    public OauthConnectionListingResponse listMyConnections() {
        String accountId = sessionInfoService.currentAccountId();
        List<OauthConnectionDto> connections = oauthConnectionService.listForAccount(accountId).stream()
                .map(connection -> oauthDtoConverter.convert(connection, null))
                .toList();
        OauthConnectionListingResponse response = new OauthConnectionListingResponse();
        response.setOauthConnectionDtoList(connections);
        return response;
    }

    public BaseResponse revokeConnection(String oauthConnectionId) {
        String accountId = sessionInfoService.currentAccountId();
        OauthConnection connection = oauthConnectionService.retrieve(oauthConnectionId);
        if (!accountId.equalsIgnoreCase(connection.getAccountId())) {
            log.warn("[OAUTH] Refusing to revoke a connection belonging to another account. accountId: {}", accountId);
            throw new NoAccessException();
        }
        oauthRefreshTokenService.revokeAllForConnection(oauthConnectionId);
        oauthConnectionService.revoke(oauthConnectionId);
        return new BaseResponse();
    }
}
