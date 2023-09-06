package co.jinear.core.model.dto.payments;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class SubscriptionInfoDto extends BaseDto {
    private ZonedDateTime cancelsAfter;
    private SubscriptionEditDto retrieveSubscriptionEditInfo;
    private List<SubscriptionPaymentInfoDto> subscriptionPaymentInfoList;
}
