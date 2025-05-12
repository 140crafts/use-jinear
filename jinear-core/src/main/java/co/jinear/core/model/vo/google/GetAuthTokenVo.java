package co.jinear.core.model.vo.google;

import co.jinear.core.model.enumtype.google.UserConsentPurposeType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetAuthTokenVo {

    private String code;
    private UserConsentPurposeType userConsentPurposeType;
}
