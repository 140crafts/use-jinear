package co.jinear.core.converter.task;

import co.jinear.core.model.request.task.InitializeChecklistRequest;
import co.jinear.core.model.vo.task.InitializeChecklistVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InitializeChecklistVoConverter {

    @Mapping(target = "title", source = "initializeChecklistRequest.title")
    @Mapping(target = "initialItemLabel", source = "initializeChecklistRequest.initialItemLabel")
    InitializeChecklistVo convert(String taskId, String ownerId, InitializeChecklistRequest initializeChecklistRequest);
}
