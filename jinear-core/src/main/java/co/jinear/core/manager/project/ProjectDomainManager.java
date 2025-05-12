package co.jinear.core.manager.project;

import co.jinear.core.model.dto.project.ProjectDomainDto;
import co.jinear.core.model.enumtype.project.ProjectDomainType;
import co.jinear.core.model.request.project.ProjectDomainInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.project.domain.ProjectDomainOperationService;
import co.jinear.core.service.project.domain.ProjectDomainRetrieveService;
import co.jinear.core.validator.project.ProjectAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectDomainManager {

    private final SessionInfoService sessionInfoService;
    private final ProjectAccessValidator projectAccessValidator;
    private final ProjectDomainOperationService projectDomainOperationService;
    private final ProjectDomainRetrieveService projectDomainRetrieveService;

    public BaseResponse initialize(ProjectDomainInitializeRequest projectDomainInitializeRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        projectAccessValidator.validateProjectIsNotArchived(projectDomainInitializeRequest.getProjectId());
        projectAccessValidator.validateHasExplicitAdminAccess(projectDomainInitializeRequest.getProjectId(), currentAccountId);
        log.info("Project domain initialize has started. currentAccountId: {}", currentAccountId);
        projectDomainOperationService.initialize(projectDomainInitializeRequest.getProjectId(), projectDomainInitializeRequest.getDomain(), ProjectDomainType.CUSTOM);
        return new BaseResponse();
    }

    public BaseResponse remove(String projectDomainId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        ProjectDomainDto projectDomainDto = projectDomainRetrieveService.retrieve(projectDomainId);
        projectAccessValidator.validateHasExplicitAdminAccess(projectDomainDto.getProjectId(), currentAccountId);
        log.info("Project domain remove has started. currentAccountId: {}", currentAccountId);
        projectDomainOperationService.remove(projectDomainId);
        return new BaseResponse();
    }
}
