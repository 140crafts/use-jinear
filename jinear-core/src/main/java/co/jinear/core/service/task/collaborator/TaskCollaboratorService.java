package co.jinear.core.service.task.collaborator;

import co.jinear.core.model.entity.task.TaskCollaborator;
import co.jinear.core.repository.task.TaskCollaboratorRepository;
import co.jinear.core.service.passive.PassiveService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskCollaboratorService {

    private TaskCollaboratorRepository taskCollaboratorRepository;
    private PassiveService passiveService;

    @Transactional
    public void upsertTaskCollaborators(String taskId, List<String> collaboratorAccountIds) {
        log.info("Upsert task collaborators has started. taskId: {}, collaboratorAccountIds: {}", taskId, collaboratorAccountIds);
        removeAllCollaborators(taskId);
        if (Objects.nonNull(collaboratorAccountIds) && !collaboratorAccountIds.isEmpty()) {
            List<TaskCollaborator> collaborators = collaboratorAccountIds.stream().map(accId -> toEntity(taskId, accId)).toList();
            taskCollaboratorRepository.saveAll(collaborators);
        }
    }

    private void removeAllCollaborators(String taskId) {
        String userActionPassive = passiveService.createUserActionPassive();
        log.info("Remove all collaborators has strted. taskId: {}, userActionPassive: {}", taskId, userActionPassive);
        taskCollaboratorRepository.updateAllPassive(taskId, userActionPassive);
    }

    private TaskCollaborator toEntity(String taskId, String collaboratorId) {
        TaskCollaborator taskCollaborator = new TaskCollaborator();
        taskCollaborator.setTaskId(taskId);
        taskCollaborator.setAccountId(collaboratorId);
        return taskCollaborator;
    }
}
