package co.jinear.core.converter.task;

import co.jinear.core.model.entity.task.TaskFeedItem;
import co.jinear.core.model.vo.task.InitializeTaskFeedItemVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InitializeTaskFeedItemVoToEntityConverter {

    TaskFeedItem convert(InitializeTaskFeedItemVo initializeTaskFeedItemVo);
}
