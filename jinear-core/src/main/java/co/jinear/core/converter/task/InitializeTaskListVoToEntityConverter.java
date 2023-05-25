package co.jinear.core.converter.task;

import co.jinear.core.model.entity.task.TaskList;
import co.jinear.core.model.enumtype.task.TaskListStateType;
import co.jinear.core.model.vo.task.InitializeTaskListVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InitializeTaskListVoToEntityConverter {

    TaskList map(InitializeTaskListVo initializeTaskListVo);

    @AfterMapping
    default void afterMap(@MappingTarget TaskList taskList) {
        taskList.setState(TaskListStateType.OPEN);
    }
}
