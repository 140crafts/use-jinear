package co.jinear.core.model.dto.messaging.message;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDataDto extends BaseDto {

    private String messageDataId;
    private String messageId;
    private String dataKey;
    private String dataValue;
}
