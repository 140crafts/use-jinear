package co.jinear.core.converter.project;

import co.jinear.core.model.request.project.InitializeMilestoneRequest;
import co.jinear.core.model.vo.project.InitializeMilestoneVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InitializeMilestoneRequestToVoConverter {

    InitializeMilestoneVo convert(InitializeMilestoneRequest initializeMilestoneRequest);
}
