package co.jinear.core.converter.project;

import co.jinear.core.model.entity.project.Project;
import co.jinear.core.model.vo.project.ProjectInitializeVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProjectInitializeVoToEntityMapper {

    @Mapping(target = "description", ignore = true)
    Project map(ProjectInitializeVo projectInitializeVo);

    @AfterMapping
    default void afterMap(@MappingTarget Project project) {
        project.setArchived(Boolean.FALSE);
    }
}
