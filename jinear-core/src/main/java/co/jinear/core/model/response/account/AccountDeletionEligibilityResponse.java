package co.jinear.core.model.response.account;

import co.jinear.core.model.dto.account.AccountDeleteEligibilityDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDeletionEligibilityResponse  extends BaseResponse {

    @JsonProperty("data")
    private AccountDeleteEligibilityDto accountDeleteEligibilityDto;
}
