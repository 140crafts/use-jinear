package co.jinear.core.converter.messaging.conversation;

import co.jinear.core.model.request.messaging.conversation.InitializeConversationRequest;
import co.jinear.core.model.vo.messaging.conversation.InitializeConversationVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface InitializeConversationRequestConverter {

    InitializeConversationVo convert(String initializedBy, InitializeConversationRequest initializeConversationRequest);

    @AfterMapping
    default void afterMap(@MappingTarget InitializeConversationVo initializeConversationVo, String initializedBy) {
        Set<String> participantAccountIds = initializeConversationVo.getParticipantAccountIds();
        participantAccountIds.add(initializedBy);
        initializeConversationVo.setParticipantAccountIds(participantAccountIds);
    }
}
