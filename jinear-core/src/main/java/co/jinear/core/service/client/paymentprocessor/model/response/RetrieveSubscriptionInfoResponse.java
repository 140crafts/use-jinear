package co.jinear.core.service.client.paymentprocessor.model.response;

import co.jinear.core.service.client.paymentprocessor.model.dto.ExternalSubscriptionInfoDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetrieveSubscriptionInfoResponse extends BaseResponse{

    @JsonProperty("data")
    private ExternalSubscriptionInfoDto subscriptionInfo;
}
