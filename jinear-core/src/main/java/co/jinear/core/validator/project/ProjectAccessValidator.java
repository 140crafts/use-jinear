package co.jinear.core.validator.project;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.project.ProjectDto;
import co.jinear.core.model.dto.project.ProjectTeamDto;
import co.jinear.core.service.project.ProjectRetrieveService;
import co.jinear.core.validator.team.TeamAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectAccessValidator {

    private final ProjectRetrieveService projectRetrieveService;
    private final TeamAccessValidator teamAccessValidator;

    /*
     * For workspace members. Not for guests!
     * */
    public void validateHasExplicitAccess(String projectId, String accountId) {
        log.info("Validate has explicit access has started. projectId: {}, accountId: {}", projectId, accountId);
        ProjectDto projectDto = projectRetrieveService.retrieve(projectId);
        validateAccountIsProjectTeamsMember(accountId, projectDto);
    }

    public void validateProjectAndWorkspaceIsInSameWorkspace(String projectId, String workspaceId) {
        if (!projectRetrieveService.checkExistsByProjectIdAndWorkspaceId(projectId, workspaceId)) {
            throw new NotFoundException();
        }
    }

    private void validateAccountIsProjectTeamsMember(String accountId, ProjectDto projectDto) {
        Optional.of(projectDto)
                .map(ProjectDto::getProjectTeams)
                .map(Collection::stream)
                .map(projectTeamDtoStream -> projectTeamDtoStream
                        .map(ProjectTeamDto::getTeamId)
                        .map(teamId -> teamAccessValidator.checkTeamAccess(accountId, projectDto.getWorkspaceId(), teamId))
                        .reduce(false, (accumulatedAccess, current) -> accumulatedAccess || current)
                )
                .filter(Boolean.TRUE::equals)
                .orElseThrow(NoAccessException::new);
    }
}
