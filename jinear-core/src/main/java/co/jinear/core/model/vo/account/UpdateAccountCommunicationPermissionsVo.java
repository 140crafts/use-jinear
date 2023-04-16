package co.jinear.core.model.vo.account;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateAccountCommunicationPermissionsVo {

    private String accountId;
    private Boolean email;
    private Boolean pushNotification;
}
