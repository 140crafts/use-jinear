package co.jinear.core.converter.project;

import co.jinear.core.model.dto.project.MilestoneInitializeDto;
import co.jinear.core.model.vo.project.InitializeMilestoneVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MilestoneInitializeDtoToInitializeMilestoneVoMapper {

    InitializeMilestoneVo map(MilestoneInitializeDto milestoneInitializeDto, String projectId);
}
