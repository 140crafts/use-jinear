package co.jinear.core.converter.messaging.message;

import co.jinear.core.model.entity.messaging.Message;
import co.jinear.core.model.vo.messaging.message.InitializeMessageVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageEntityConverter {

    Message convert(InitializeMessageVo initializeMessageVo, String richTextId);
}
