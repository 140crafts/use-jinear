package co.jinear.core.model.vo.messaging.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class InitializeMessageVo {

    private String accountId;
    private String threadId;
    private String conversationId;
    private String body;
    private Map<String, String> data;
}
