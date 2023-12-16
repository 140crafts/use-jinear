package co.jinear.core.converter.task;

import co.jinear.core.converter.feed.FeedDtoConverter;
import co.jinear.core.model.dto.task.TaskFeedItemDto;
import co.jinear.core.model.entity.task.TaskFeedItem;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {FeedDtoConverter.class})
public interface TaskFeedItemToDtoConverter {

    TaskFeedItemDto convert(TaskFeedItem taskFeedItem);
}
