package co.jinear.core.controller.messaging.conversation;

import co.jinear.core.manager.messaging.ConversationManager;
import co.jinear.core.model.request.messaging.conversation.InitializeConversationRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.messaging.ConversationParticipantListingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/messaging/conversation")
public class ConversationController {

    private final ConversationManager conversationManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse initialize(@Valid @RequestBody InitializeConversationRequest initializeConversationRequest) {
        return conversationManager.initialize(initializeConversationRequest);
    }

    @GetMapping("/participated/{workspaceId}")
    @ResponseStatus(HttpStatus.OK)
    public ConversationParticipantListingResponse retrieveParticipated(@PathVariable String workspaceId) {
        return conversationManager.retrieveParticipated(workspaceId);
    }
}
