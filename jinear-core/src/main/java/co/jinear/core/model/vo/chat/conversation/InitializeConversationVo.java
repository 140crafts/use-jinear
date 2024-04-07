package co.jinear.core.model.vo.chat.conversation;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InitializeConversationVo {

    private List<String> participantAccountIds;
}
