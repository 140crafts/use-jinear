package co.jinear.core.manager.oauth.provider;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.oauth.OauthConnectionDto;
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
    private final SessionInfoService sessionInfoService;

    public OauthConnectionListingResponse listMyConnections() {
        String accountId = sessionInfoService.currentAccountId();
        OauthConnectionListingResponse response = new OauthConnectionListingResponse();
        response.setOauthConnectionDtoList(oauthConnectionService.listForAccount(accountId));
        return response;
    }

    public BaseResponse revokeConnection(String oauthConnectionId) {
        OauthConnectionDto connection = oauthConnectionService.retrieve(oauthConnectionId);
        sessionInfoService.validateOwnership(connection.getAccountId());
        oauthRefreshTokenService.revokeAllForConnection(oauthConnectionId);
        oauthConnectionService.revoke(oauthConnectionId);
        return new BaseResponse();
    }
}
