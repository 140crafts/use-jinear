package co.jinear.core.validator.workspace;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceTier;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class WorkspaceTierValidator {

    private final WorkspaceRetrieveService workspaceRetrieveService;

    public void validateWorkspaceTier(String workspaceId, WorkspaceTier workspaceTier) {
        log.info("Validate workspace tier has started. workspaceId: {}, workspaceTier: {}", workspaceId, workspaceTier);
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithId(workspaceId);
        validateWorkspaceTier(workspaceDto, workspaceTier);
    }

    public void validateWorkspaceTier(WorkspaceDto workspaceDto, WorkspaceTier workspaceTier) {
        log.info("Validate workspace tier has started. workspaceId: {}, workspaceTier: {}", workspaceDto.getWorkspaceId(), workspaceTier);
        if (Objects.isNull(workspaceDto.getTier()) || !workspaceTier.equals(workspaceDto.getTier())) {
            throw new NoAccessException();
        }
    }
}
