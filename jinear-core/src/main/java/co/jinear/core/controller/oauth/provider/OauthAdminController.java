package co.jinear.core.controller.oauth.provider;

import co.jinear.core.manager.oauth.provider.OauthAdminManager;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.oauth.OauthClientListingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "v1/admin/oauth")
@RequiredArgsConstructor
public class OauthAdminController {

    private final OauthAdminManager oauthAdminManager;

    @GetMapping("/client/list")
    public OauthClientListingResponse listClients(@RequestParam(defaultValue = "0") Integer page) {
        return oauthAdminManager.listClients(page);
    }

    @DeleteMapping("/client")
    public BaseResponse revokeClient(@RequestParam String clientId) {
        return oauthAdminManager.revokeClient(clientId);
    }
}
