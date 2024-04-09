package co.jinear.core.model.dto.messaging.thread;

import co.jinear.core.model.dto.messaging.message.MessageDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThreadMessageInfoDto {

    private String threadId;
    private String initialMessageId;
    private String latestMessageId;
    private MessageDto initialMessage;
    private MessageDto latestMessage;
}
