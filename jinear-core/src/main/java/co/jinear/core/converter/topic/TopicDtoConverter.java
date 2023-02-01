package co.jinear.core.converter.topic;

import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.entity.topic.Topic;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TopicDtoConverter {

    TopicDto map(Topic topic);
}
