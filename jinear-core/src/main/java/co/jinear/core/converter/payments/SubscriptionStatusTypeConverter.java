package co.jinear.core.converter.payments;

import co.jinear.core.service.client.paymentprocessor.model.enumtype.SubscriptionStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class SubscriptionStatusTypeConverter implements AttributeConverter<SubscriptionStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(SubscriptionStatus type) {
        return Optional.ofNullable(type).map(SubscriptionStatus::getValue).orElse(null);
    }

    @Override
    public SubscriptionStatus convertToEntityAttribute(Integer integer) {
        return Arrays.stream(SubscriptionStatus.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
