package co.jinear.core.model.dto.messaging.conversation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConversationParticipantDto extends PlainConversationParticipantDto {

    private ConversationDto conversation;
}
