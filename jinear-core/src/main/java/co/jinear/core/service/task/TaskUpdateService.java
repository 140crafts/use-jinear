package co.jinear.core.service.task;

import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.entity.task.Task;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.model.vo.richtext.InitializeRichTextVo;
import co.jinear.core.model.vo.richtext.UpdateRichTextVo;
import co.jinear.core.model.vo.task.TaskDescriptionUpdateVo;
import co.jinear.core.model.vo.task.TaskTitleUpdateVo;
import co.jinear.core.repository.TaskRepository;
import co.jinear.core.service.richtext.RichTextInitializeService;
import co.jinear.core.service.team.workflow.TeamWorkflowStatusRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskUpdateService {

    private final TaskRetrieveService taskRetrieveService;
    private final TaskRepository taskRepository;
    private final TeamWorkflowStatusRetrieveService workflowStatusRetrieveService;
    private final RichTextInitializeService richTextInitializeService;
    private final ModelMapper modelMapper;

    public TaskDto updateTaskTitle(TaskTitleUpdateVo taskTitleUpdateVo) {
        log.info("Update task title has started. taskTitleUpdateVo: {}", taskTitleUpdateVo);
        Task task = taskRetrieveService.retrieveEntity(taskTitleUpdateVo.getTaskId());
        task.setTitle(taskTitleUpdateVo.getTitle());
        Task saved = taskRepository.save(task);
        log.info("Update task title has finished. taskId: {}", saved.getTaskId());
        return modelMapper.map(saved, TaskDto.class);
    }

    public TaskDto updateTaskDescription(TaskDescriptionUpdateVo taskDescriptionUpdateVo) {
        log.info("Update task description has started. taskDescriptionUpdateVo: {}", taskDescriptionUpdateVo);
        TaskDto taskDto = taskRetrieveService.retrieve(taskDescriptionUpdateVo.getTaskId());
        RichTextDto richTextDto = Optional.of(taskDto)
                .map(TaskDto::getDescription)
                .map(richText -> {
                    UpdateRichTextVo updateRichTextVo = new UpdateRichTextVo();
                    updateRichTextVo.setRichTextId(richText.getRichTextId());
                    updateRichTextVo.setValue(taskDescriptionUpdateVo.getDescription());
                    updateRichTextVo.setType(RichTextType.TASK_DETAIL);
                    return richTextInitializeService.historicallyUpdateRichTextBody(updateRichTextVo);
                })
                .orElseGet(() -> {
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
        return modelMapper.map(saved, TaskDto.class);
    }

    private void validateWorkflowExists(String workflowStatusId) {
        workflowStatusRetrieveService.retrieve(workflowStatusId);
    }
}
