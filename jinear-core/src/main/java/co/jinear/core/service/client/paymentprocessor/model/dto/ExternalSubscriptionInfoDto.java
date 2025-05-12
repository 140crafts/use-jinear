package co.jinear.core.service.client.paymentprocessor.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class ExternalSubscriptionInfoDto {
    private ZonedDateTime cancelsAfter;
    private ExternalSubscriptionEditDto retrieveSubscriptionEditInfo;
    private List<ExternalSubscriptionPaymentInfoDto> subscriptionPaymentInfoList;
}
