package co.jinear.core.model.dto.google;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Set;

@Getter
@Setter
public class GoogleTokenDto extends BaseDto {

    private String googleTokenId;
    private ZonedDateTime expiresAt;
    private String accessToken;
    private String refreshToken;
    private String idToken;
    private String tokenType;
    private String googleUserInfoId;
    private Set<GoogleTokenScopeDto> scopes;
    private GoogleUserInfoDto googleUserInfo;
}
