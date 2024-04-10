package co.jinear.core.model.dto.messaging.conversation;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ConversationDto {

    private String conversationId;
    private String workspaceId;
    private ZonedDateTime lastActivityTime;
    private ConversationMessageInfoDto conversationMessageInfo;
}
