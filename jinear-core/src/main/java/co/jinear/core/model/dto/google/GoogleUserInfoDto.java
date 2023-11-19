package co.jinear.core.model.dto.google;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleUserInfoDto extends BaseDto {

    private String googleUserInfoId;
    private String sub;
    private String email;
    private String emailVerified;
    private String name;
    private String picture;
    private String givenName;
    private String familyName;
    private String locale;
}
