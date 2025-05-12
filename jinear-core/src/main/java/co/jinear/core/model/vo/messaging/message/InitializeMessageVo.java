package co.jinear.core.model.vo.messaging.message;

import co.jinear.core.model.enumtype.messaging.MessageType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class InitializeMessageVo {

    private String accountId;
    private String robotId;
    private String threadId;
    private String conversationId;
    private String body;
    private Map<String, String> data;
    private MessageType messageType = MessageType.USER_MESSAGE;
}
