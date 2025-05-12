package co.jinear.core.system.util.payment;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.service.client.paymentprocessor.model.dto.PassthroughDetailDto;
import co.jinear.core.service.client.paymentprocessor.model.enumtype.PassthroughType;
import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class PassthroughHelper {

    public String retrievePassthroughValue(Set<PassthroughDetailDto> passthroughDetails, PassthroughType passthroughType) {
        return passthroughDetails.stream()
                .filter(passthroughDetailDto -> passthroughType.equals(passthroughDetailDto.getPassthroughType()))
                .findFirst()
                .map(PassthroughDetailDto::getDetailValue)
                .orElseThrow(BusinessException::new);
    }
}
