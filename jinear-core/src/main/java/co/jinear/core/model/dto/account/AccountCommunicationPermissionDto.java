package co.jinear.core.model.dto.account;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class AccountCommunicationPermissionDto extends BaseDto {

    private String accountId;
    private Boolean email;
    private Boolean pushNotification;
}
