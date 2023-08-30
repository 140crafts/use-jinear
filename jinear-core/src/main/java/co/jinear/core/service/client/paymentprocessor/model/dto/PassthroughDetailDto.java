package co.jinear.core.service.client.paymentprocessor.model.dto;

import co.jinear.core.service.client.paymentprocessor.model.enumtype.PassthroughType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassthroughDetailDto {

    private PassthroughType passthroughType;
    private String detailValue;
}
