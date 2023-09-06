package co.jinear.core.service.client.paymentprocessor.model.enumtype;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatusType {
    SUCCESS("success"),
    FAILURE("failure");

    private final String value;
}
