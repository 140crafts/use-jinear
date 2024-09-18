package co.jinear.core.manager.project;

import co.jinear.core.converter.project.InitializeMilestoneRequestToVoConverter;
import co.jinear.core.model.dto.project.MilestoneDto;
import co.jinear.core.model.dto.project.ProjectDto;
import co.jinear.core.model.request.project.InitializeMilestoneRequest;
import co.jinear.core.model.request.project.MilestoneUpdateRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.vo.project.InitializeMilestoneVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.project.MilestoneOperationService;
import co.jinear.core.service.project.MilestoneRetrieveService;
import co.jinear.core.service.project.ProjectRetrieveService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectMilestoneManager {

    private final SessionInfoService sessionInfoService;
    private final MilestoneOperationService milestoneOperationService;
    private final InitializeMilestoneRequestToVoConverter initializeMilestoneRequestToVoConverter;
    private final WorkspaceValidator workspaceValidator;
    private final ProjectRetrieveService projectRetrieveService;
    private final MilestoneRetrieveService milestoneRetrieveService;
    private final PassiveService passiveService;

    public BaseResponse initialize(InitializeMilestoneRequest initializeMilestoneRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        ProjectDto projectDto = projectRetrieveService.retrieve(initializeMilestoneRequest.getProjectId());
        workspaceValidator.validateHasAccess(currentAccountId, projectDto.getWorkspaceId());
        log.info("Initialize milestone has started. currentAccountId: {}", currentAccountId);
        InitializeMilestoneVo initializeMilestoneVo = initializeMilestoneRequestToVoConverter.convert(initializeMilestoneRequest);
        milestoneOperationService.initialize(initializeMilestoneVo);
        return new BaseResponse();
    }

    public BaseResponse updateTitle(MilestoneUpdateRequest milestoneUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(milestoneUpdateRequest.getMilestoneId(), currentAccountId);
        log.info("Update milestone title has started. currentAccountId: {}", currentAccountId);
        milestoneOperationService.updateTitle(milestoneUpdateRequest.getMilestoneId(), milestoneUpdateRequest.getTitle());
        return new BaseResponse();
    }

    public BaseResponse updateDescription(MilestoneUpdateRequest milestoneUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(milestoneUpdateRequest.getMilestoneId(), currentAccountId);
        log.info("Update milestone description has started. currentAccountId: {}", currentAccountId);
        milestoneOperationService.updateDescription(milestoneUpdateRequest.getMilestoneId(), milestoneUpdateRequest.getDescription());
        return new BaseResponse();
    }

    public BaseResponse updateTargetDate(MilestoneUpdateRequest milestoneUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(milestoneUpdateRequest.getMilestoneId(), currentAccountId);
        log.info("Update milestone target date has started. currentAccountId: {}", currentAccountId);
        milestoneOperationService.updateTargetDate(milestoneUpdateRequest.getMilestoneId(), milestoneUpdateRequest.getTargetDate());
        return new BaseResponse();
    }

    public BaseResponse updateOrder(MilestoneUpdateRequest milestoneUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(milestoneUpdateRequest.getMilestoneId(), currentAccountId);
        log.info("Update milestone order has started. currentAccountId: {}", currentAccountId);
        milestoneOperationService.updateOrder(milestoneUpdateRequest.getMilestoneId(), milestoneUpdateRequest.getOrder());
        return new BaseResponse();
    }

    public BaseResponse deleteMilestone(String milestoneId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateWorkspaceAccess(milestoneId, currentAccountId);
        log.info("Delete milestone has started. currentAccountId: {}", currentAccountId);
        String passiveId = milestoneOperationService.passivize(milestoneId);
        passiveService.assignOwnership(passiveId, currentAccountId);
        return new BaseResponse();
    }

    private void validateWorkspaceAccess(String milestoneId, String currentAccountId) {
        MilestoneDto milestoneDto = milestoneRetrieveService.retrieve(milestoneId);
        ProjectDto projectDto = projectRetrieveService.retrieve(milestoneDto.getProjectId());
        workspaceValidator.validateHasAccess(currentAccountId, projectDto.getWorkspaceId());
    }
}
