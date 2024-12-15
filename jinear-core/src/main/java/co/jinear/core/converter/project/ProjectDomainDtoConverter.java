package co.jinear.core.converter.project;

import co.jinear.core.model.dto.project.ProjectDomainDto;
import co.jinear.core.model.entity.project.ProjectDomain;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectDomainDtoConverter {

    ProjectDomainDto convert(ProjectDomain projectDomain);
}
