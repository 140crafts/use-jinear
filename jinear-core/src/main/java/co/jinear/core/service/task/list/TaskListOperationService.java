package co.jinear.core.service.task.list;

import co.jinear.core.converter.task.InitializeTaskListVoToEntityConverter;
import co.jinear.core.converter.task.TaskListDtoConverter;
import co.jinear.core.model.dto.task.TaskListDto;
import co.jinear.core.model.entity.task.TaskList;
import co.jinear.core.model.vo.task.InitializeTaskListVo;
import co.jinear.core.model.vo.task.UpdateTaskListDueDateVo;
import co.jinear.core.model.vo.task.UpdateTaskListStateVo;
import co.jinear.core.model.vo.task.UpdateTaskListTitleVo;
import co.jinear.core.repository.TaskListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskListOperationService {

    private final TaskListRepository taskListRepository;
    private final InitializeTaskListVoToEntityConverter initializeTaskListVoToEntityConverter;
    private final TaskListDtoConverter taskListDtoConverter;
    private final TaskListRetrieveService taskListRetrieveService;

    public TaskListDto initializeTaskList(InitializeTaskListVo initializeTaskListVo) {
        log.info("Initialize task list has started. initializeTaskListVo: {}", initializeTaskListVo);
        TaskList taskList = initializeTaskListVoToEntityConverter.map(initializeTaskListVo);
        TaskList saved = taskListRepository.save(taskList);
        return taskListDtoConverter.convert(saved);
    }

    public TaskListDto updateDueDate(UpdateTaskListDueDateVo updateTaskListDueDateVo) {
        log.info("Update task list due date has started. updateTaskListDueDateVo: {}", updateTaskListDueDateVo);
        TaskList taskList = taskListRetrieveService.retrieveEntity(updateTaskListDueDateVo.getTaskListId());
        taskList.setDueDate(updateTaskListDueDateVo.getDueDate());
        TaskList saved = taskListRepository.save(taskList);
        return taskListDtoConverter.convert(saved);
    }

    public TaskListDto updateTitle(UpdateTaskListTitleVo updateTaskListTitleVo) {
        log.info("Update task list title has started. updateTaskListTitleVo: {}", updateTaskListTitleVo);
        TaskList taskList = taskListRetrieveService.retrieveEntity(updateTaskListTitleVo.getTaskListId());
        taskList.setTitle(updateTaskListTitleVo.getTitle());
        TaskList saved = taskListRepository.save(taskList);
        return taskListDtoConverter.convert(saved);
    }

    public TaskListDto updateState(UpdateTaskListStateVo updateTaskListStateVo) {
        log.info("Update task list state has started. updateTaskListStateVo: {}", updateTaskListStateVo);
        TaskList taskList = taskListRetrieveService.retrieveEntity(updateTaskListStateVo.getTaskListId());
        taskList.setState(updateTaskListStateVo.getState());
        TaskList saved = taskListRepository.save(taskList);
        return taskListDtoConverter.convert(saved);
    }
}
