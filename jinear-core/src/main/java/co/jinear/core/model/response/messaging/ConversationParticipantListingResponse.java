package co.jinear.core.model.response.messaging;

import co.jinear.core.model.dto.messaging.conversation.ConversationParticipantDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConversationParticipantListingResponse extends BaseResponse {

    @JsonProperty("data")
    List<ConversationParticipantDto> participations;
}
