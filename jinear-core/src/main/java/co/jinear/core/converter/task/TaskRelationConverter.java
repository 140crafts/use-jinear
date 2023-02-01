package co.jinear.core.converter.task;

import co.jinear.core.model.dto.task.TaskRelationDto;
import co.jinear.core.model.entity.task.TaskRelation;
import co.jinear.core.model.vo.task.TaskRelationInitializeVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskRelationConverter {

    TaskRelation map(TaskRelationInitializeVo taskRelationInitializeVo);

    TaskRelationDto map(TaskRelation taskRelation);
}
