package co.jinear.core.service.task;

import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.entity.task.Task;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import co.jinear.core.model.vo.richtext.InitializeRichTextVo;
import co.jinear.core.model.vo.task.TaskInitializeVo;
import co.jinear.core.repository.TaskRepository;
import co.jinear.core.service.richtext.RichTextInitializeService;
import co.jinear.core.service.team.TeamLockService;
import co.jinear.core.service.team.workflow.TeamWorkflowStatusRetrieveService;
import co.jinear.core.service.topic.TopicSequenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskInitializeService {

    private final TaskRetrieveService taskRetrieveService;
    private final TaskRepository taskRepository;
    private final TaskLockService taskLockService;
    private final TeamLockService teamLockService;
    private final TeamWorkflowStatusRetrieveService teamWorkflowStatusRetrieveService;
    private final TopicSequenceService incrementTopicSequence;
    private final RichTextInitializeService richTextInitializeService;
    private final ModelMapper modelMapper;

    @Transactional
    public TaskDto initializeTask(TaskInitializeVo taskInitializeVo) {
        log.info("Initialize task has started. taskInitializeVo: {}", taskInitializeVo);
        assignLocks(taskInitializeVo);
        try {
            Task task = mapVoToEntity(taskInitializeVo);
            assignTeamTaskNo(task);
            assignTopicTaskNo(task);
            assignInitialWorkflowStatus(task);
            Task saved = taskRepository.saveAndFlush(task);
            TaskDto taskDto = modelMapper.map(saved, TaskDto.class);
            initializeAndAssignRichText(taskInitializeVo, taskDto);
            return taskDto;
        } finally {
            releaseLocks(taskInitializeVo);
        }
    }

    private void assignLocks(TaskInitializeVo taskInitializeVo) {
        teamLockService.lockTeamForTaskInitialization(taskInitializeVo.getTeamId());
        Optional.of(taskInitializeVo)
                .map(TaskInitializeVo::getTopicId)
                .ifPresent(taskLockService::lockTopicForTaskInitialization);
    }

    private void releaseLocks(TaskInitializeVo taskInitializeVo) {
        teamLockService.unlockTeamForTaskInitialization(taskInitializeVo.getTeamId());
        Optional.of(taskInitializeVo)
                .map(TaskInitializeVo::getTopicId)
                .ifPresent(taskLockService::unlockTopicForTaskInitialization);
    }

    private void assignTopicTaskNo(Task task) {
        String topicId = task.getTopicId();
        if (Objects.nonNull(topicId)) {
            Integer nextSeq = incrementTopicSequence.incrementTopicSequence(topicId);
            task.setTopicTagNo(nextSeq);
        }
    }

    private void assignTeamTaskNo(Task task) {
        Long count = taskRetrieveService.countAllByTeamId(task.getTeamId());
        task.setTeamTagNo(count.intValue() + 1);
    }

    private void assignInitialWorkflowStatus(Task task) {
        TeamWorkflowStatusDto teamWorkflowStatusDto = teamWorkflowStatusRetrieveService.retrieveFirstFromGroup(task.getTeamId(), TeamWorkflowStateGroup.BACKLOG);
        task.setWorkflowStatusId(teamWorkflowStatusDto.getTeamWorkflowStatusId());
    }

    private void initializeAndAssignRichText(TaskInitializeVo taskInitializeVo, TaskDto taskDto) {
        Optional.of(taskInitializeVo)
                .map(TaskInitializeVo::getDescription)
                .map(desc -> {
                    InitializeRichTextVo initializeRichTextVo = new InitializeRichTextVo();
                    initializeRichTextVo.setRelatedObjectId(taskDto.getTaskId());
                    initializeRichTextVo.setValue(taskInitializeVo.getDescription());
                    initializeRichTextVo.setType(RichTextType.TASK_DETAIL);
                    return richTextInitializeService.initializeRichText(initializeRichTextVo);
                })
                .ifPresent(taskDto::setDescription);
    }

    private Task mapVoToEntity(TaskInitializeVo taskInitializeVo) {
        Task task = new Task();
        task.setTopicId(taskInitializeVo.getTopicId());
        task.setWorkspaceId(taskInitializeVo.getWorkspaceId());
        task.setTeamId(taskInitializeVo.getTeamId());
        task.setOwnerId(taskInitializeVo.getOwnerId());
        task.setAssignedTo(taskInitializeVo.getAssignedTo());
        task.setAssignedDate(taskInitializeVo.getAssignedDate());
        task.setDueDate(taskInitializeVo.getDueDate());
        task.setTitle(taskInitializeVo.getTitle());
        return task;
    }
}
