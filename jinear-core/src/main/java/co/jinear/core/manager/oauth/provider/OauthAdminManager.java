package co.jinear.core.manager.oauth.provider;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.oauth.OauthClientDto;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.oauth.OauthClientListingResponse;
import co.jinear.core.service.oauth.provider.OauthClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthAdminManager {

    private static final int PAGE_SIZE = 100;

    private final OauthClientService oauthClientService;

    public OauthClientListingResponse listClients(int page) {
        Page<OauthClientDto> clients = oauthClientService.listClients(PageRequest.of(page, PAGE_SIZE));
        OauthClientListingResponse response = new OauthClientListingResponse();
        response.setOauthClientDtoPage(new PageDto<>(clients));
        return response;
    }

    public BaseResponse revokeClient(String clientId) {
        oauthClientService.revokeClient(clientId);
        return new BaseResponse();
    }
}
