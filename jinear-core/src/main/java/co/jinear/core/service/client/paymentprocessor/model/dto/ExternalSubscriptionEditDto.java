package co.jinear.core.service.client.paymentprocessor.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExternalSubscriptionEditDto extends BaseDto {
    private String cancelUrl;
    private String updateUrl;
}
