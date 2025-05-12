package co.jinear.core.manager.project;

import co.jinear.core.converter.project.ProjectFeedSettingsOperationRequestToVoConverter;
import co.jinear.core.model.request.project.ProjectFeedSettingsOperationRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.vo.project.ProjectFeedSettingsOperationVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.project.ProjectFeedSettingsOperationService;
import co.jinear.core.validator.project.ProjectAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectFeedSettingsManager {

    private final SessionInfoService sessionInfoService;
    private final ProjectFeedSettingsOperationService projectFeedSettingsOperationService;
    private final ProjectAccessValidator projectAccessValidator;
    private final ProjectFeedSettingsOperationRequestToVoConverter projectFeedSettingsOperationRequestToVoConverter;

    public BaseResponse update(ProjectFeedSettingsOperationRequest projectFeedSettingsOperationRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        projectAccessValidator.validateHasExplicitAdminAccess(projectFeedSettingsOperationRequest.getProjectId(), currentAccountId);
        log.info("Project feed settings update has started. currentAccountId: {}", currentAccountId);
        ProjectFeedSettingsOperationVo projectFeedSettingsOperationVo = projectFeedSettingsOperationRequestToVoConverter.convert(projectFeedSettingsOperationRequest);
        projectFeedSettingsOperationService.update(projectFeedSettingsOperationVo);
        return new BaseResponse();
    }
}
