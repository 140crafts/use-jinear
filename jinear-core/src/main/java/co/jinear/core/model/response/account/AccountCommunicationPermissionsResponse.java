package co.jinear.core.model.response.account;

import co.jinear.core.model.dto.account.AccountCommunicationPermissionDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCommunicationPermissionsResponse extends BaseResponse {

    @JsonProperty("data")
    private AccountCommunicationPermissionDto accountCommunicationPermissionDto;
}
