package co.jinear.core.converter.task;

import co.jinear.core.model.entity.task.TaskBoard;
import co.jinear.core.model.enumtype.task.TaskBoardStateType;
import co.jinear.core.model.vo.task.InitializeTaskBoardVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InitializeTaskListVoToEntityConverter {

    TaskBoard map(InitializeTaskBoardVo initializeTaskBoardVo);

    @AfterMapping
    default void afterMap(@MappingTarget TaskBoard taskBoard) {
        taskBoard.setState(TaskBoardStateType.OPEN);
    }
}
