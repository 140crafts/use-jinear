package co.jinear.core.converter.topic;

import co.jinear.core.model.request.topic.TopicUpdateRequest;
import co.jinear.core.model.vo.topic.TopicUpdateVo;
import co.jinear.core.system.NormalizeHelper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface TopicUpdateVoConverter {

    TopicUpdateVo map(TopicUpdateRequest topicUpdateRequest);

    @AfterMapping
    default void afterMap(@MappingTarget TopicUpdateVo topicUpdateVo, TopicUpdateRequest topicUpdateRequest) {
        Optional.of(topicUpdateRequest)
                .map(TopicUpdateRequest::getTag)
                .map(NormalizeHelper::normalizeStrictly)
                .ifPresent(topicUpdateVo::setTag);
    }
}
