package co.jinear.core.converter.topic;

import co.jinear.core.model.request.topic.TopicInitializeRequest;
import co.jinear.core.model.vo.topic.TopicInitializeVo;
import co.jinear.core.system.NormalizeHelper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TopicInitializeVoConverter {

    TopicInitializeVo map(TopicInitializeRequest topicInitializeRequest, String ownerId);

    @AfterMapping
    default void afterMap(@MappingTarget TopicInitializeVo topicInitializeVo, TopicInitializeRequest topicInitializeRequest, String ownerId) {
        topicInitializeVo.setTag(NormalizeHelper.normalizeStrictly(topicInitializeRequest.getTag()));
        topicInitializeVo.setOwnerId(ownerId);
    }
}
