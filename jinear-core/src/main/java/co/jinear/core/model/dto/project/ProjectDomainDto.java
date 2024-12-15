package co.jinear.core.model.dto.project;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.project.ProjectDomainCnameCheckResultType;
import co.jinear.core.model.enumtype.project.ProjectDomainType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDomainDto extends BaseDto {

    private String projectDomainId;
    private String projectId;
    private String domain;
    private ProjectDomainType domainType;
    private ProjectDomainCnameCheckResultType cnameCheckResult;
}
