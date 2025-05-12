package co.jinear.core.validator.team;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.system.NormalizeHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamValidator {

    private final TeamRetrieveService teamRetrieveService;

    public void validateTeamNameIsNotUsedInWorkspace(String teamName, String workspaceId) {
        log.info("Validating team name is not used in workspace before. teamName: {}, workspaceId: {}", teamName, workspaceId);
        teamRetrieveService.retrieveTeamIncludingPassivesByNameOptional(teamName, workspaceId)
                .ifPresent(teamDto -> {
                    throw new BusinessException("workspace.team.team-name-is-taken");
                });
    }

    public void validateTeamTagIsNotUsedInWorkspace(String teamTag, String workspaceId) {
        log.info("Validating team tag is not used in workspace before. teamTag: {}, workspaceId: {}", teamTag, workspaceId);
        teamRetrieveService.retrieveTeamByTagOptional(teamTag, workspaceId)
                .ifPresent(teamDto -> {
                    throw new BusinessException("workspace.team.team-tag-is-taken");
                });
    }

    public void validateTeamUsernameIsNotUsedInWorkspace(String username, String workspaceId) {
        log.info("Validating team username is not used in workspace before. teamUsername: {}, workspaceId: {}", username, workspaceId);
        teamRetrieveService.retrieveTeamByUsernameOptional(username, workspaceId)
                .ifPresent(teamDto -> {
                    throw new BusinessException("workspace.team.team-username-is-taken");
                });
    }

    public void validateAllExistsAndActiveWithinSameWorkspace(String workspaceId, List<String> teamIds) {
        log.info("Validate all exists and active within same workspace has started. workspaceId: {}, teamIds: {}", workspaceId, NormalizeHelper.listToString(teamIds));
        if (Boolean.FALSE.equals(teamRetrieveService.checkAllExistsAndActiveWithinSameWorkspace(workspaceId, teamIds))) {
            throw new NotFoundException();
        }
    }
}
