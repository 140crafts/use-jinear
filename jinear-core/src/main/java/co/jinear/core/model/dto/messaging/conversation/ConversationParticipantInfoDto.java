package co.jinear.core.model.dto.messaging.conversation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConversationParticipantInfoDto {

    private String conversationParticipantId;
    private String conversationId;
    private Long unreadCount;
}
