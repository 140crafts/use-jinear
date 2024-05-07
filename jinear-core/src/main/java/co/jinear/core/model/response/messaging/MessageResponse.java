package co.jinear.core.model.response.messaging;

import co.jinear.core.model.dto.messaging.message.MessageDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse extends BaseResponse {

    @JsonProperty("data")
    private MessageDto messageDto;
}
