package co.jinear.core.model.response.payments;

import co.jinear.core.model.dto.payments.SubscriptionInfoDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetrieveSubscriptionInfoResponse extends BaseResponse {

    @JsonProperty("data")
    private SubscriptionInfoDto subscriptionInfoDto;
}
