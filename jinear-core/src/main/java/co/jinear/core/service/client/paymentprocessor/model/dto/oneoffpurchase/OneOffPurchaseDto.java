package co.jinear.core.service.client.paymentprocessor.model.dto.oneoffpurchase;

import co.jinear.core.service.client.paymentprocessor.model.dto.BaseDto;
import co.jinear.core.service.client.paymentprocessor.model.enumtype.PaymentProviderType;
import co.jinear.core.service.client.paymentprocessor.model.enumtype.ProductType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class OneOffPurchaseDto extends BaseDto {

    private String oneOffPurchaseId;
    private String externalId;
    private PaymentProviderType provider;
    private ProductType product;
    private Boolean refunded;
    private String relatedObjectId;
}
