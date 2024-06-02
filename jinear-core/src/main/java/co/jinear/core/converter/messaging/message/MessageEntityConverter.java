package co.jinear.core.converter.messaging.message;

import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.entity.messaging.Message;
import co.jinear.core.model.vo.messaging.message.InitializeMessageVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface MessageEntityConverter {

    Message convert(InitializeMessageVo initializeMessageVo, Optional<RichTextDto> richTextDtoOptional);

    @AfterMapping
    default void afterMap(@MappingTarget Message message, Optional<RichTextDto> richTextDtoOptional) {
        richTextDtoOptional.map(RichTextDto::getRichTextId)
                .ifPresent(message::setRichTextId);
    }
}
