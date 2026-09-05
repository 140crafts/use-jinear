package co.jinear.core.controller.oauth.provider;

import co.jinear.core.manager.oauth.provider.OauthConnectionManager;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.oauth.OauthConnectionListingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "v1/oauth")
@RequiredArgsConstructor
public class OauthConnectionController {

    private final OauthConnectionManager oauthConnectionManager;

    @GetMapping("/connection/list")
    public OauthConnectionListingResponse listMyConnections() {
        return oauthConnectionManager.listMyConnections();
    }

    @DeleteMapping("/connection/{oauthConnectionId}")
    public BaseResponse revokeConnection(@PathVariable String oauthConnectionId) {
        return oauthConnectionManager.revokeConnection(oauthConnectionId);
    }
}
