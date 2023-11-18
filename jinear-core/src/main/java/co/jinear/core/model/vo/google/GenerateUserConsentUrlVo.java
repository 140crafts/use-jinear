package co.jinear.core.model.vo.google;

import co.jinear.core.model.enumtype.google.UserConsentPurposeType;
import co.jinear.core.system.gcloud.auth.model.enumtype.AccessType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GenerateUserConsentUrlVo {

    private UserConsentPurposeType userConsentPurposeType;
    private String responseType = "code";
    private AccessType accessType = AccessType.OFFLINE;
    private String includeGrantedScopes = "true";
    private String state = "state_parameter_passthrough_value";
    private Boolean includeCalendarScopes=Boolean.FALSE;
    private Boolean includeEmailScopes=Boolean.FALSE;
}
