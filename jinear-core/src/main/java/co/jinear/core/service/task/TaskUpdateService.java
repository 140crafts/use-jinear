package co.jinear.core.service.task;

import co.jinear.core.converter.task.TaskDtoConverter;
import co.jinear.core.converter.task.TaskSubscriptionConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.task.UpdateTaskWorkflowDto;
import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.entity.task.Task;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.model.enumtype.task.TaskReminderType;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import co.jinear.core.model.vo.richtext.InitializeRichTextVo;
import co.jinear.core.model.vo.richtext.UpdateRichTextVo;
import co.jinear.core.model.vo.task.*;
import co.jinear.core.repository.TaskRepository;
import co.jinear.core.service.richtext.RichTextInitializeService;
import co.jinear.core.service.task.reminder.TaskReminderDateUpdateService;
import co.jinear.core.service.task.reminder.TaskReminderOperationService;
import co.jinear.core.service.task.subscription.TaskSubscriptionOperationService;
import co.jinear.core.service.team.workflow.TeamWorkflowStatusRetrieveService;
import co.jinear.core.service.topic.TopicSequenceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup.CANCELLED;
import static co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup.COMPLETED;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskUpdateService {

    private static final List<TeamWorkflowStateGroup> REMOVES_REMINDERS_ON_THESE_STATUSES = List.of(COMPLETED, CANCELLED);

    private final TaskRetrieveService taskRetrieveService;
    private final TaskRepository taskRepository;
    private final TeamWorkflowStatusRetrieveService workflowStatusRetrieveService;
    private final RichTextInitializeService richTextInitializeService;
    private final TaskLockService taskLockService;
    private final TopicSequenceService incrementTopicSequence;
    private final TaskSubscriptionConverter taskSubscriptionConverter;
    private final TaskSubscriptionOperationService taskSubscriptionOperationService;
    private final TaskReminderDateUpdateService taskReminderDateUpdateService;
    private final TaskReminderOperationService taskReminderOperationService;
    private final TaskSearchService taskSearchService;

    private final TaskDtoConverter taskDtoConverter;

    public TaskDto updateTaskTitle(TaskTitleUpdateVo taskTitleUpdateVo) {
        log.info("Update task title has started. taskTitleUpdateVo: {}", taskTitleUpdateVo);
        Task task = taskRetrieveService.retrieveEntity(taskTitleUpdateVo.getTaskId());
        task.setTitle(taskTitleUpdateVo.getTitle());
        Task saved = taskRepository.save(task);
        taskSearchService.refreshTaskFtsMv();
        log.info("Update task title has finished. taskId: {}", saved.getTaskId());
        return taskDtoConverter.map(saved);
    }

    public TaskDto updateTaskDescription(TaskDescriptionUpdateVo taskDescriptionUpdateVo) {
        log.info("Update task description has started. taskDescriptionUpdateVo: {}", taskDescriptionUpdateVo);
        TaskDto taskDto = taskRetrieveService.retrieve(taskDescriptionUpdateVo.getTaskId());
        RichTextDto richTextDto = Optional.of(taskDto).map(TaskDto::getDescription).map(richText -> {
            UpdateRichTextVo updateRichTextVo = new UpdateRichTextVo();
            updateRichTextVo.setRichTextId(richText.getRichTextId());
            updateRichTextVo.setValue(taskDescriptionUpdateVo.getDescription());
            updateRichTextVo.setType(RichTextType.TASK_DETAIL);
            return richTextInitializeService.historicallyUpdateRichTextBody(updateRichTextVo);
        }).orElseGet(() -> {
            InitializeRichTextVo initializeRichTextVo = new InitializeRichTextVo();
            initializeRichTextVo.setRelatedObjectId(taskDto.getTaskId());
            initializeRichTextVo.setValue(taskDescriptionUpdateVo.getDescription());
            initializeRichTextVo.setType(RichTextType.TASK_DETAIL);
            return richTextInitializeService.initializeRichText(initializeRichTextVo);
        });
        taskDto.setDescription(richTextDto);
        taskSearchService.refreshTaskFtsMv();
        return taskDto;
    }

    @Transactional
    public UpdateTaskWorkflowDto updateTaskWorkflow(String taskId, String workflowStatusId) {
        log.info("Update task workflow status has started for taskId: {}, workflowStatusId: {}", taskId, workflowStatusId);
        String remindersPassiveId = validateWorkflowStatusAndCancelReminders(workflowStatusId, taskId);
        Task task = taskRetrieveService.retrieveEntity(taskId);
        task.setWorkflowStatusId(workflowStatusId);
        Task saved = taskRepository.save(task);
        log.info("Update task workflow status has finished. taskId: {}", saved.getTaskId());
        TaskDto taskDto = taskDtoConverter.map(saved);
        return taskDtoConverter.map(taskDto, remindersPassiveId);
    }

    @Transactional
    public TaskDto updateTaskTopic(String taskId, String topicId) {
        log.info("Update task topic has started for taskId: {}, topicId: {}", taskId, topicId);
        Task task = taskRetrieveService.retrieveEntity(taskId);
        lockTaskTopicForUpdate(topicId);
        try {
            task.setTopicId(topicId);
            assignTopicTaskNo(task);
            Task saved = taskRepository.saveAndFlush(task);
            return taskDtoConverter.map(saved);
        } finally {
            unlockTaskTopicForUpdate(topicId);
        }
    }

    public TaskDto updateTaskDates(TaskDatesUpdateVo taskDatesUpdateVo) {
        log.info("Update task assigned and due date has started. taskDatesUpdateVo: {}", taskDatesUpdateVo);
        Task task = taskRetrieveService.retrieveEntity(taskDatesUpdateVo.getTaskId());
        task.setAssignedDate(taskDatesUpdateVo.getAssignedDate());
        task.setDueDate(taskDatesUpdateVo.getDueDate());
        task.setHasPreciseAssignedDate(taskDatesUpdateVo.getHasPreciseAssignedDate());
        task.setHasPreciseDueDate(taskDatesUpdateVo.getHasPreciseDueDate());
        Task saved = taskRepository.save(task);
        updateRelatedReminderDates(saved);
        log.info("Update task assigned and due date has finished");
        return taskDtoConverter.map(saved);
    }

    public TaskDto updateTaskAssignee(TaskAssigneeUpdateVo taskAssigneeUpdateVo) {
        log.info("Update task assignee has started. taskDatesUpdateVo: {}", taskAssigneeUpdateVo);
        Task task = taskRetrieveService.retrieveEntity(taskAssigneeUpdateVo.getTaskId());
        task.setAssignedTo(taskAssigneeUpdateVo.getAssigneeId());
        Task saved = taskRepository.save(task);
        initializeSubscription(saved);
        taskSearchService.refreshTaskFtsMv();
        log.info("Update task assignee has finished");
        return taskDtoConverter.map(saved);
    }

    public TaskDto updateTaskProjectAndMilestone(String taskId, String projectId, String milestoneId) {
        log.info("Update task project has started. taskId: {}, projectId: {}, milestoneId: {}", taskId, projectId, milestoneId);
        validateEitherOneIsBlankOrBothFilled(projectId, milestoneId);
        Task task = taskRetrieveService.retrieveEntity(taskId);
        task.setProjectId(projectId);
        task.setMilestoneId(milestoneId);
        Task saved = taskRepository.save(task);
        log.info("Update task project has finished");
        return taskDtoConverter.map(saved);
    }

    public void updateAllMilestoneIdsAndProjectIdsAsNullWithMilestoneId(String milestoneId) {
        log.info("Update all milestone ids and project ids as null with milestone id has started. milestoneId: {}", milestoneId);
        taskRepository.updateAllMilestoneIdsAndProjectIdsAsNullWithMilestoneId(milestoneId);
    }

    private static void validateEitherOneIsBlankOrBothFilled(String projectId, String milestoneId) {
        if ((StringUtils.isBlank(projectId) && StringUtils.isNotBlank(milestoneId)) ||
            (StringUtils.isNotBlank(projectId) && StringUtils.isBlank(milestoneId))) {
            throw new BusinessException();
        }
    }

    private String validateWorkflowStatusAndCancelReminders(String workflowStatusId, String taskId) {
        TeamWorkflowStatusDto teamWorkflowStatusDto = workflowStatusRetrieveService.retrieve(workflowStatusId);
        if (REMOVES_REMINDERS_ON_THESE_STATUSES.contains(teamWorkflowStatusDto.getWorkflowStateGroup())) {
            return taskReminderOperationService.passivizeAllWithRelatedTask(taskId);
        }
        return null;
    }

    private void lockTaskTopicForUpdate(String topicId) {
        Optional.ofNullable(topicId).ifPresent(taskLockService::lockTopicForTaskInitialization);
    }

    private void unlockTaskTopicForUpdate(String topicId) {
        Optional.ofNullable(topicId).ifPresent(taskLockService::unlockTopicForTaskInitialization);
    }

    private void assignTopicTaskNo(Task task) {
        String topicId = task.getTopicId();
        Integer nextSeq = Optional.ofNullable(topicId)
                .map(incrementTopicSequence::incrementTopicSequence)
                .orElse(null);
        task.setTopicTagNo(nextSeq);
    }

    private void initializeSubscription(Task task) {
        if (Objects.nonNull(task.getAssignedTo())) {
            TaskSubscriptionInitializeVo assigneeSubscriptionInitializeVo = taskSubscriptionConverter.map(task.getAssignedTo(), task.getTaskId());
            taskSubscriptionOperationService.initializeTaskSubscription(assigneeSubscriptionInitializeVo);
        }
    }

    private void updateRelatedReminderDates(Task task) {
        taskReminderDateUpdateService.updateTaskReminderWithTypeIfExists(task.getTaskId(), TaskReminderType.ASSIGNED_DATE, task.getAssignedDate());
        taskReminderDateUpdateService.updateTaskReminderWithTypeIfExists(task.getTaskId(), TaskReminderType.DUE_DATE, task.getDueDate());
    }
}
