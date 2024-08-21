package co.jinear.core.converter.project;

import co.jinear.core.model.entity.project.Project;
import co.jinear.core.model.vo.project.ProjectInitializeVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectInitializeVoToEntityMapper {

    @Mapping(target = "description", ignore = true)
    Project map(ProjectInitializeVo projectInitializeVo);
}
