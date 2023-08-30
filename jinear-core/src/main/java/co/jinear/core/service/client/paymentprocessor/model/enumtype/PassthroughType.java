package co.jinear.core.service.client.paymentprocessor.model.enumtype;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PassthroughType {
    ACCOUNT_ID(0),
    WORKSPACE_ID(1),
    EMAIL(2),
    EMAIL_CONFIRMED(3);

    private final int value;
}
