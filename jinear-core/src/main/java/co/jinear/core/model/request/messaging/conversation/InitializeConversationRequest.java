package co.jinear.core.model.request.messaging.conversation;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class InitializeConversationRequest extends BaseRequest {

    @NotNull
    private String workspaceId;
    @NotNull
    private String initialMessageBody;
    @NotEmpty
    private List<String> participantAccountIds;
}
