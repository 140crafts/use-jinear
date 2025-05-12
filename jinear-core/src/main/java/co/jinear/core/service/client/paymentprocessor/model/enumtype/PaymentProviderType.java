package co.jinear.core.service.client.paymentprocessor.model.enumtype;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentProviderType {

    PADDLE(0);

    private final int value;
}
