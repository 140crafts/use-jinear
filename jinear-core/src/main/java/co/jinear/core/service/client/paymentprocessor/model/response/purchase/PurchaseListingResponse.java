package co.jinear.core.service.client.paymentprocessor.model.response.purchase;

import co.jinear.core.service.client.paymentprocessor.model.dto.purchase.PurchaseListingDto;
import co.jinear.core.service.client.paymentprocessor.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseListingResponse extends BaseResponse {

    @JsonProperty("data")
    private PurchaseListingDto purchaseListingDto;
}
