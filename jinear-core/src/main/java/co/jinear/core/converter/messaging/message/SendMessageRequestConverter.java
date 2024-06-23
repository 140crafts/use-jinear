package co.jinear.core.converter.messaging.message;

import co.jinear.core.model.request.messaging.message.SendMessageRequest;
import co.jinear.core.model.vo.messaging.message.InitializeMessageVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SendMessageRequestConverter {

    InitializeMessageVo convertForThread(String accountId, String threadId, SendMessageRequest sendMessageRequest);

    InitializeMessageVo convertForThreadRobot(String robotId, String threadId, SendMessageRequest sendMessageRequest);

    @Mapping(target = "conversationId", source = "conversationId")
    InitializeMessageVo convertForConversation(String accountId, String conversationId, SendMessageRequest sendMessageRequest);
}
