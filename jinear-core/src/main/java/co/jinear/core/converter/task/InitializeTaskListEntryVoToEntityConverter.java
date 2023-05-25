package co.jinear.core.converter.task;

import co.jinear.core.model.entity.task.TaskListEntry;
import co.jinear.core.model.vo.task.InitializeTaskListEntryVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InitializeTaskListEntryVoToEntityConverter {

    TaskListEntry map(InitializeTaskListEntryVo initializeTaskListEntryVo, Integer order);
}
