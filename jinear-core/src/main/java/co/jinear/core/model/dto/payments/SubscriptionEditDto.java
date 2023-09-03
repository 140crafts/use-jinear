package co.jinear.core.model.dto.payments;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubscriptionEditDto extends BaseDto {
    private String cancelUrl;
    private String updateUrl;
}
