package co.jinear.core.converter.task;

import co.jinear.core.model.request.task.TaskRetrieveIntersectingRequest;
import co.jinear.core.model.vo.task.SearchIntersectingTasksVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SearchIntersectingTasksVoConverter {

    SearchIntersectingTasksVo map(TaskRetrieveIntersectingRequest taskRetrieveIntersectingRequest);
}
