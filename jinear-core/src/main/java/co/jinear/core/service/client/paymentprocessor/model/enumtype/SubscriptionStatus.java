package co.jinear.core.service.client.paymentprocessor.model.enumtype;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubscriptionStatus {
    ACTIVE(1),
    TRIALING(2),
    PAST_DUE(3),
    PAUSED(4),
    DELETED(5);

    private final int value;
}
