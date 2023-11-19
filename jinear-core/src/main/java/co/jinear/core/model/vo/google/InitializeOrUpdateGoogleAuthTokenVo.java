package co.jinear.core.model.vo.google;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitializeOrUpdateGoogleAuthTokenVo {

    private String googleUserInfoId;
    private String accessToken;
    private Integer expiresIn;
    private String refreshToken;
    private String scope;
    private String tokenType;
    private String idToken;
}
