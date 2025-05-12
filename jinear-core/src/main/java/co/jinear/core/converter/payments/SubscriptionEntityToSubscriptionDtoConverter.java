package co.jinear.core.converter.payments;

import co.jinear.core.model.dto.payments.SubscriptionDto;
import co.jinear.core.model.entity.payments.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionEntityToSubscriptionDtoConverter {
    SubscriptionDto map(Subscription subscription);
}
