package co.jinear.core.manager.oauth.provider;

import co.jinear.core.converter.oauth.OauthDtoConverter;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.oauth.OauthClientDto;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.oauth.OauthClientListingResponse;
import co.jinear.core.service.oauth.provider.OauthClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthAdminManager {

    private static final int PAGE_SIZE = 25;

    private final OauthClientService oauthClientService;
    private final OauthDtoConverter oauthDtoConverter;

    public OauthClientListingResponse listClients(int page) {
        var clients = oauthClientService.listClients(PageRequest.of(page, PAGE_SIZE))
                .map(oauthDtoConverter::convert);
        OauthClientListingResponse response = new OauthClientListingResponse();
        response.setOauthClientDtoPage(new PageDto<OauthClientDto>(clients));
        return response;
    }

    public BaseResponse revokeClient(String clientId) {
        oauthClientService.revokeClient(clientId);
        return new BaseResponse();
    }
}
