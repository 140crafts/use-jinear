package co.jinear.core.converter.topic;

import co.jinear.core.model.request.topic.TopicInitializeRequest;
import co.jinear.core.model.vo.topic.TopicInitializeVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TopicInitializeVoConverter {

    TopicInitializeVo map(TopicInitializeRequest topicInitializeRequest,String ownerId);

    @AfterMapping
    default void afterMap(@MappingTarget TopicInitializeVo topicInitializeVo,String ownerId){
        topicInitializeVo.setOwnerId(ownerId);
    }
}
