package co.jinear.core.model.dto.payments;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString(callSuper = true)
public class SubscriptionPaymentInfoDto extends BaseDto {
    private String relatedEntityId;
    private String balanceGross;
    private String currency;
    private String receiptUrl;
    private ZonedDateTime parsedEventTime;
    private ZonedDateTime parsedNextBillDate;
}
