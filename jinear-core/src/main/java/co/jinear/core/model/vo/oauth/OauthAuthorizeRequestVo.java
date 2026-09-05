package co.jinear.core.model.vo.oauth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@Builder
@ToString
public class OauthAuthorizeRequestVo {

    private String responseType;
    private String clientId;
    private String redirectUri;
    private String scope;
    private String state;
    private String codeChallenge;
    private String codeChallengeMethod;
    private String resource;

    public static OauthAuthorizeRequestVo fromParams(Map<String, String> params) {
        return OauthAuthorizeRequestVo.builder()
                .responseType(params.get("response_type"))
                .clientId(params.get("client_id"))
                .redirectUri(params.get("redirect_uri"))
                .scope(params.get("scope"))
                .state(params.get("state"))
                .codeChallenge(params.get("code_challenge"))
                .codeChallengeMethod(params.get("code_challenge_method"))
                .resource(params.get("resource"))
                .build();
    }
}
