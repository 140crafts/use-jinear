package co.jinear.core.service.client.paymentprocessor.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString(callSuper = true)
public class ExternalSubscriptionPaymentInfoDto extends BaseDto {
    private String relatedEntityId;
    private String balanceGross;
    private String currency;
    private String receiptUrl;
    private ZonedDateTime parsedEventTime;
    private ZonedDateTime parsedNextBillDate;
}
