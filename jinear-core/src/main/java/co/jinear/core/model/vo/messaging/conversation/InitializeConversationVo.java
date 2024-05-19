package co.jinear.core.model.vo.messaging.conversation;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class InitializeConversationVo {

    private String workspaceId;
    private String initializedBy;
    private String initialMessageBody;
    private Set<String> participantAccountIds;
}
