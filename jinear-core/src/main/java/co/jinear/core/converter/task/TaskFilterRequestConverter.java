package co.jinear.core.converter.task;

import co.jinear.core.model.request.task.TaskFilterRequest;
import co.jinear.core.model.vo.task.TaskSearchFilterVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskFilterRequestConverter {

    TaskSearchFilterVo convert(TaskFilterRequest taskFilterRequest);
}
