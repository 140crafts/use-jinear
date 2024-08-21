package co.jinear.core.converter.project;

import co.jinear.core.model.entity.project.Milestone;
import co.jinear.core.model.vo.project.InitializeMilestoneVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InitializeMilestoneVoToEntityConverter {

    @Mapping(target = "description", ignore = true)
    Milestone convert(InitializeMilestoneVo initializeMilestoneVo);
}
