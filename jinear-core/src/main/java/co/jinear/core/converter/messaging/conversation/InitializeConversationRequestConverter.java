package co.jinear.core.converter.messaging.conversation;

import co.jinear.core.model.request.messaging.conversation.InitializeConversationRequest;
import co.jinear.core.model.vo.messaging.conversation.InitializeConversationVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InitializeConversationRequestConverter {

    InitializeConversationVo convert(String initializedBy, InitializeConversationRequest initializeConversationRequest);
}
