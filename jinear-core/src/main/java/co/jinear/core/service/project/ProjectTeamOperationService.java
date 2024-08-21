package co.jinear.core.service.project;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.entity.project.ProjectTeam;
import co.jinear.core.repository.project.ProjectTeamRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.task.TaskListingService;
import jakarta.transaction.Transactional;
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
        ProjectTeam projectTeam = mapEntity(projectId, teamId);
        projectTeamRepository.save(projectTeam);
    }

    @Transactional
    public String remove(String projectId, String teamId) {
        log.info("Remove team from project has started. projectId: {}, teamId: {}", projectId, teamId);
        validateProjectHasNoTasksWithThisTeam(projectId, teamId);
        ProjectTeam projectTeam = retrieveEntity(projectId, teamId);
        String passiveId = passiveService.createUserActionPassive();
        projectTeam.setPassiveId(passiveId);
        projectTeamRepository.save(projectTeam);
        return passiveId;
    }

    private ProjectTeam retrieveEntity(String projectId, String teamId) {
        log.info("Retrieve entity has started. projectId: {}, teamId: {}", projectId, teamId);
        return projectTeamRepository.findByProjectIdAndTeamIdAndPassiveIdIsNull(projectId, teamId)
                .orElseThrow(NotFoundException::new);
    }

    private ProjectTeam mapEntity(String projectId, String teamId) {
        ProjectTeam projectTeam = new ProjectTeam();
        projectTeam.setProjectId(projectId);
        projectTeam.setTeamId(teamId);
        return projectTeam;
    }

    private void validateProjectHasNoTasksWithThisTeam(String projectId, String teamId) {
        boolean anyTaskExistsWithTeamIdAndProjectId = taskListingService.checkAnyActiveTaskExistsWithTeamAndProject(teamId, projectId);
        if (anyTaskExistsWithTeamIdAndProjectId) {
            throw new BusinessException();
        }
    }
}
