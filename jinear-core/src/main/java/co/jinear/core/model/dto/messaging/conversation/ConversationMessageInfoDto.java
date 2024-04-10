package co.jinear.core.model.dto.messaging.conversation;

import co.jinear.core.model.dto.messaging.message.MessageDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConversationMessageInfoDto {

    private String conversationId;
    private String lastMessageId;
    private MessageDto lastMessage;
}
