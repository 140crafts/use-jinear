package co.jinear.core.converter.task;

import co.jinear.core.model.request.task.TaskInitializeRequest;
import co.jinear.core.model.vo.task.TaskInitializeVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskInitializeVoConverter {

    TaskInitializeVo map(TaskInitializeRequest taskInitializeRequest);
}
