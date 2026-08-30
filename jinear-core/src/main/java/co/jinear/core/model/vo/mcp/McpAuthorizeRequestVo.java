package co.jinear.core.model.vo.mcp;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class McpAuthorizeRequestVo {

    private String responseType;
    private String clientId;
    private String redirectUri;
    private String scope;
    private String state;
    private String codeChallenge;
    private String codeChallengeMethod;
    private String resource;
}
