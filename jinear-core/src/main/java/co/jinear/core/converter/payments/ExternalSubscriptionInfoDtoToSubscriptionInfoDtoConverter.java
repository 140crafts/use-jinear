package co.jinear.core.converter.payments;

import co.jinear.core.model.dto.payments.SubscriptionInfoDto;
import co.jinear.core.service.client.paymentprocessor.model.dto.ExternalSubscriptionInfoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExternalSubscriptionInfoDtoToSubscriptionInfoDtoConverter {

    SubscriptionInfoDto convert(ExternalSubscriptionInfoDto subscriptionInfoDto);
}
