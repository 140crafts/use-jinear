package co.jinear.core.service.client.paymentprocessor.model.dto;

import co.jinear.core.service.client.paymentprocessor.model.enumtype.PassthroughType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PassthroughDetailDto {

    private PassthroughType passthroughType;
    private String detailValue;
}
