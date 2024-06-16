package co.jinear.core.service.task;

import co.jinear.core.converter.task.TaskDtoConverter;
import co.jinear.core.converter.task.TaskSubscriptionConverter;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.entity.task.Task;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.model.enumtype.task.TaskRelationType;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import co.jinear.core.model.vo.richtext.InitializeRichTextVo;
import co.jinear.core.model.vo.task.*;
import co.jinear.core.repository.TaskRepository;
import co.jinear.core.service.richtext.RichTextInitializeService;
import co.jinear.core.service.task.board.entry.TaskBoardEntryOperationService;
import co.jinear.core.service.task.feed.TaskFeedItemOperationService;
import co.jinear.core.service.task.relation.TaskRelationInitializeService;
import co.jinear.core.service.task.subscription.TaskSubscriptionOperationService;
import co.jinear.core.service.team.TeamLockService;
import co.jinear.core.service.team.workflow.TeamWorkflowStatusRetrieveService;
import co.jinear.core.service.topic.TopicSequenceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskInitializeService {

    private final TaskRepository taskRepository;
    private final TaskLockService taskLockService;
    private final TeamLockService teamLockService;
    private final TeamWorkflowStatusRetrieveService teamWorkflowStatusRetrieveService;
    private final TopicSequenceService incrementTopicSequence;
    private final RichTextInitializeService richTextInitializeService;
    private final TaskDtoConverter taskDtoConverter;
    private final TaskRelationInitializeService taskRelationInitializeService;
    private final TaskSubscriptionConverter taskSubscriptionConverter;
    private final TaskSubscriptionOperationService taskSubscriptionOperationService;
    private final TaskBoardEntryOperationService taskBoardEntryOperationService;
    private final TaskFeedItemOperationService taskFeedItemOperationService;
    private final TaskAnalyticsService taskAnalyticsService;
    private final TaskSearchService taskSearchService;

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
            TaskDto taskDto = taskDtoConverter.map(saved);
            initializeAndAssignRichText(taskInitializeVo, taskDto);
            initializeSubtaskRelation(taskInitializeVo, saved);
            initializeTaskSubscription(taskInitializeVo, saved);
            initializeTaskBoardEntry(taskInitializeVo, task);
            initializeTaskFeedItem(taskInitializeVo, task);
            taskSearchService.refreshTaskFtsMv();
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
        Long count = taskAnalyticsService.retrieveMaxTeamTagNo(task.getTeamId());
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
        task.setHasPreciseAssignedDate(taskInitializeVo.getHasPreciseAssignedDate());
        task.setHasPreciseDueDate(taskInitializeVo.getHasPreciseDueDate());
        task.setDueDate(taskInitializeVo.getDueDate());
        task.setTitle(taskInitializeVo.getTitle());
        return task;
    }

    private void initializeSubtaskRelation(TaskInitializeVo taskInitializeVo, Task saved) {
        String subTaskOf = taskInitializeVo.getSubTaskOf();
        if (Objects.nonNull(subTaskOf)) {
            TaskRelationInitializeVo taskRelationInitializeVo = new TaskRelationInitializeVo();
            taskRelationInitializeVo.setTaskId(saved.getTaskId());
            taskRelationInitializeVo.setRelatedTaskId(subTaskOf);
            taskRelationInitializeVo.setRelation(TaskRelationType.SUBTASK);
            taskRelationInitializeVo.setWorkspaceId(saved.getWorkspaceId());
            taskRelationInitializeVo.setTeamId(saved.getTeamId());
            taskRelationInitializeVo.setPerformedBy(taskInitializeVo.getOwnerId());
            taskRelationInitializeService.initializeTaskRelation(taskRelationInitializeVo);
        }
    }

    private void initializeTaskSubscription(TaskInitializeVo taskInitializeVo, Task saved) {
        TaskSubscriptionInitializeVo ownerSubscriptionInitializeVo = taskSubscriptionConverter.map(taskInitializeVo.getOwnerId(), saved.getTaskId());
        taskSubscriptionOperationService.initializeTaskSubscription(ownerSubscriptionInitializeVo);
        if (Objects.nonNull(taskInitializeVo.getAssignedTo()) && !Objects.equals(taskInitializeVo.getOwnerId(), taskInitializeVo.getAssignedTo())) {
            log.info("Assignee is different from owner. Adding assignee to subscriptions.");
            TaskSubscriptionInitializeVo assigneeSubscriptionInitializeVo = taskSubscriptionConverter.map(taskInitializeVo.getAssignedTo(), saved.getTaskId());
            taskSubscriptionOperationService.initializeTaskSubscription(assigneeSubscriptionInitializeVo);
        }
    }

    private void initializeTaskBoardEntry(TaskInitializeVo taskInitializeVo, Task task) {
        String boardId = taskInitializeVo.getBoardId();
        if (Objects.nonNull(boardId)) {
            InitializeTaskBoardEntryVo initializeTaskBoardEntryVo = new InitializeTaskBoardEntryVo();
            initializeTaskBoardEntryVo.setTaskId(task.getTaskId());
            initializeTaskBoardEntryVo.setTaskBoardId(boardId);
            taskBoardEntryOperationService.initialize(initializeTaskBoardEntryVo);
        }
    }

    private void initializeTaskFeedItem(TaskInitializeVo taskInitializeVo, Task task) {
        if (Objects.nonNull(taskInitializeVo.getFeedId()) && Objects.nonNull(taskInitializeVo.getFeedItemId())) {
            InitializeTaskFeedItemVo initializeTaskFeedItemVo = new InitializeTaskFeedItemVo(task.getTaskId(), taskInitializeVo.getFeedId(), taskInitializeVo.getFeedItemId());
            taskFeedItemOperationService.initializeTaskFeedItemRelation(initializeTaskFeedItemVo);
        }
    }
}
