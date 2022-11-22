package co.jinear.core.model.response.account;

import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountRetrieveResponse extends BaseResponse {

    @JsonProperty("data")
    private AccountDto accountDto;
}
