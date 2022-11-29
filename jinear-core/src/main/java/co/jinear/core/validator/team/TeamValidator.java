package co.jinear.core.validator.team;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.service.team.TeamRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamValidator {

    private final TeamRetrieveService teamRetrieveService;

    public void validateTeamNameIsNotUsedInWorkspace(String teamName, String workspaceId) {
        log.info("Validating team name is not used in workspace before. teamName: {}, workspaceId: {}", teamName, workspaceId);
        teamRetrieveService.retrieveTeamByNameOptional(teamName, workspaceId)
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
}
