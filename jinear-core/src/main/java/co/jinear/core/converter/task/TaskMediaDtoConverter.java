package co.jinear.core.converter.task;

import co.jinear.core.converter.media.MediaDtoConverter;
import co.jinear.core.model.dto.task.TaskMediaDto;
import co.jinear.core.model.vo.task.TaskMediaVo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {TaskDtoConverter.class, MediaDtoConverter.class})
public interface TaskMediaDtoConverter {

    TaskMediaDto convert(TaskMediaVo taskMediaVo);
}
