package co.jinear.core.service.task;

import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.entity.task.Task;
import co.jinear.core.model.vo.task.TaskUpdateVo;
import co.jinear.core.repository.TaskRepository;
import co.jinear.core.service.team.workflow.TeamWorkflowStatusRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskUpdateService {

    private final TaskRetrieveService taskRetrieveService;
    private final TaskRepository taskRepository;
    private final TeamWorkflowStatusRetrieveService workflowStatusRetrieveService;
    private final ModelMapper modelMapper;

    public TaskDto updateTask(TaskUpdateVo taskUpdateVo) {
        log.info("Update task has started. taskUpdateVo: {}", taskUpdateVo);
        Task task = taskRetrieveService.retrieveEntity(taskUpdateVo.getTaskId());
        updateValues(task, taskUpdateVo);
        Task saved = taskRepository.save(task);
        log.info("Update task has finished. taskId: {}", saved.getTaskId());
        return modelMapper.map(saved, TaskDto.class);
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

    private void updateValues(Task task, TaskUpdateVo taskUpdateVo) {
        task.setTopicId(taskUpdateVo.getTopicId());
        task.setAssignedDate(taskUpdateVo.getAssignedDate());
        task.setDueDate(taskUpdateVo.getDueDate());
        task.setTitle(taskUpdateVo.getTitle());
        task.setDescription(taskUpdateVo.getDescription());
    }

    private void validateWorkflowExists(String workflowStatusId) {
        workflowStatusRetrieveService.retrieve(workflowStatusId);
    }
}
