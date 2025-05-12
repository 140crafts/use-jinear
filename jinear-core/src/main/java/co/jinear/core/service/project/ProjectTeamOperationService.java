package co.jinear.core.service.project;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.entity.project.ProjectTeam;
import co.jinear.core.repository.project.ProjectTeamRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.task.TaskListingService;
import co.jinear.core.system.NormalizeHelper;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectTeamOperationService {

    private final ProjectTeamRepository projectTeamRepository;
    private final PassiveService passiveService;
    private final TaskListingService taskListingService;

    public void initializeAll(String projectId, List<String> teamIds) {
        log.info("Initialize all project team has started. projectId: {}", projectId);
        if (Objects.nonNull(teamIds)) {
            teamIds.forEach(teamId -> initialize(projectId, teamId));
        }
    }

    public void initialize(String projectId, String teamId) {
        log.info("Initialize project team has started. projectId: {}, teamId: {}", projectId, teamId);
        boolean exists = projectTeamRepository.existsByProjectIdAndTeamIdAndPassiveIdIsNull(projectId, teamId);
        if (exists) {
            log.info("Project team already exists!");
            return;
        }
        ProjectTeam projectTeam = mapEntity(projectId, teamId);
        projectTeamRepository.save(projectTeam);
    }

    @Transactional
    public String updateAs(String projectId, List<String> teamIds) {
        log.info("Update project teams as has started. projectId: {}, teamIds: {}", projectId, NormalizeHelper.listToString(teamIds));

        List<ProjectTeam> projectTeamsToBeRemoved = projectTeamRepository.findAllByProjectIdAndTeamIdIsNotInAndPassiveIdIsNull(projectId, teamIds);
        String passiveId = validateAndRemove(projectId, projectTeamsToBeRemoved);

        List<String> teamIdsToBeAdded = filterOutAlreadyExistingTeamsAndRetrieveToBeAdded(projectId, teamIds);
        initializeAll(projectId, teamIdsToBeAdded);

        return passiveId;
    }

    @NonNull
    private List<String> filterOutAlreadyExistingTeamsAndRetrieveToBeAdded(String projectId, List<String> teamIds) {
        List<ProjectTeam> projectTeamsAlreadyExists = projectTeamRepository.findAllByProjectIdAndTeamIdIsInAndPassiveIdIsNull(projectId, teamIds);
        List<String> projectTeamIdsAlreadyExists = projectTeamsAlreadyExists.stream().map(ProjectTeam::getTeamId).toList();
        return teamIds.stream().filter(teamId -> !projectTeamIdsAlreadyExists.contains(teamId)).toList();
    }

    private String validateAndRemove(String projectId, List<ProjectTeam> projectTeamsToBeRemoved) {
        validateProjectHasNoTasksWithGivenTeams(projectId, projectTeamsToBeRemoved);
        String passiveId = projectTeamsToBeRemoved.isEmpty() ? null : passiveService.createUserActionPassive();
        projectTeamsToBeRemoved.forEach(projectTeam -> {
            projectTeam.setPassiveId(passiveId);
            projectTeamRepository.save(projectTeam);
        });
        return passiveId;
    }

    private ProjectTeam mapEntity(String projectId, String teamId) {
        ProjectTeam projectTeam = new ProjectTeam();
        projectTeam.setProjectId(projectId);
        projectTeam.setTeamId(teamId);
        return projectTeam;
    }

    private void validateProjectHasNoTasksWithGivenTeams(String projectId, List<ProjectTeam> teamsToBeRemoved) {
        List<String> teamIdsToBeRemoved = teamsToBeRemoved.stream().map(ProjectTeam::getTeamId).toList();
        boolean anyTaskExistsWithGivenTeamIdsAndProjectId = taskListingService.checkAnyActiveTaskExistsWithGivenTeamsAndProject(teamIdsToBeRemoved, projectId);
        if (anyTaskExistsWithGivenTeamIdsAndProjectId) {
            throw new BusinessException("project.teams.remove.task-exists");
        }
    }
}
