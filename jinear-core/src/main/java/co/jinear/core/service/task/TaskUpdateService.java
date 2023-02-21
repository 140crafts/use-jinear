package co.jinear.core.service.task;

import co.jinear.core.converter.task.TaskDtoConverter;
import co.jinear.core.converter.task.TaskSubscriptionConverter;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.entity.task.Task;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.model.vo.richtext.InitializeRichTextVo;
import co.jinear.core.model.vo.richtext.UpdateRichTextVo;
import co.jinear.core.model.vo.task.*;
import co.jinear.core.repository.TaskRepository;
import co.jinear.core.service.richtext.RichTextInitializeService;
import co.jinear.core.service.task.subscription.TaskSubscriptionOperationService;
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
public class TaskUpdateService {

    private final TaskRetrieveService taskRetrieveService;
    private final TaskRepository taskRepository;
    private final TeamWorkflowStatusRetrieveService workflowStatusRetrieveService;
    private final RichTextInitializeService richTextInitializeService;
    private final TaskLockService taskLockService;
    private final TopicSequenceService incrementTopicSequence;
    private final TaskSubscriptionConverter taskSubscriptionConverter;
    private final TaskSubscriptionOperationService taskSubscriptionOperationService;

    private final TaskDtoConverter taskDtoConverter;

    public TaskDto updateTaskTitle(TaskTitleUpdateVo taskTitleUpdateVo) {
        log.info("Update task title has started. taskTitleUpdateVo: {}", taskTitleUpdateVo);
        Task task = taskRetrieveService.retrieveEntity(taskTitleUpdateVo.getTaskId());
        task.setTitle(taskTitleUpdateVo.getTitle());
        Task saved = taskRepository.save(task);
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
        return taskDto;
    }

    public TaskDto updateTaskWorkflow(String taskId, String workflowStatusId) {
        log.info("Update task workflow status has started for taskId: {}, workflowStatusId: {}", taskId, workflowStatusId);
        validateWorkflowExists(workflowStatusId);
        Task task = taskRetrieveService.retrieveEntity(taskId);
        task.setWorkflowStatusId(workflowStatusId);
        Task saved = taskRepository.save(task);
        log.info("Update task workflow status has finished. taskId: {}", saved.getTaskId());
        return taskDtoConverter.map(saved);
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
        Task saved = taskRepository.save(task);
        log.info("Update task assigned and due date has finished");
        return taskDtoConverter.map(saved);
    }

    public TaskDto updateTaskAssignee(TaskAssigneeUpdateVo taskAssigneeUpdateVo) {
        log.info("Update task assignee has started. taskDatesUpdateVo: {}", taskAssigneeUpdateVo);
        Task task = taskRetrieveService.retrieveEntity(taskAssigneeUpdateVo.getTaskId());
        task.setAssignedTo(taskAssigneeUpdateVo.getAssigneeId());
        Task saved = taskRepository.save(task);
        initializeSubscription(saved);
        log.info("Update task assignee has finished");
        return taskDtoConverter.map(saved);
    }

    private void validateWorkflowExists(String workflowStatusId) {
        workflowStatusRetrieveService.retrieve(workflowStatusId);
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

    private void initializeSubscription(Task saved) {
        if (Objects.nonNull(saved.getAssignedTo())) {
            TaskSubscriptionInitializeVo assigneeSubscriptionInitializeVo = taskSubscriptionConverter.map(saved.getAssignedTo(), saved.getTaskId());
            taskSubscriptionOperationService.initializeTaskSubscription(assigneeSubscriptionInitializeVo);
        }
    }
}
