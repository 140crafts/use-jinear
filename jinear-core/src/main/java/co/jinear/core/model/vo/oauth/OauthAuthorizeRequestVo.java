package co.jinear.core.model.vo.oauth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * The query parameters of an /authorize call.
 * <p>
 * The wire names are snake_case because RFC 6749 fixes them, and Spring's data binder
 * matches query parameters to JavaBean property names with no naming strategy in
 * between. Rather than name the fields snake_case and carry that through the manager,
 * the translation happens once in {@link #fromParams}, which keeps every wire name in
 * one place next to the field it fills.
 */
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

    /**
     * Absent parameters stay null. Validating them is the authorization manager's job,
     * because most of them have to be reported back to the client as an OAuth error
     * redirect rather than refused here.
     */
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
