package co.jinear.core.converter.topic;

import co.jinear.core.model.request.topic.TopicUpdateRequest;
import co.jinear.core.model.vo.topic.TopicUpdateVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TopicUpdateVoConverter {

    TopicUpdateVo map(TopicUpdateRequest topicUpdateRequest);
}
