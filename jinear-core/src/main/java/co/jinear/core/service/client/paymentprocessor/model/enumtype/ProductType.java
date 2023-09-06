package co.jinear.core.service.client.paymentprocessor.model.enumtype;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductType {
    WIE(0),
    JINEAR(1);

    private final int value;
}
