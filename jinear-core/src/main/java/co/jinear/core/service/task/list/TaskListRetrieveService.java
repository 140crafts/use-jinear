package co.jinear.core.service.task.list;

import co.jinear.core.converter.task.TaskListDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.task.TaskListDto;
import co.jinear.core.model.entity.task.TaskList;
import co.jinear.core.repository.TaskListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskListRetrieveService {

    private final TaskListRepository taskListRepository;
    private final TaskListDtoConverter taskListDtoConverter;

    public TaskList retrieveEntity(String taskListId) {
        log.info("Retrieve task list entity has started. taskListId: {}", taskListId);
        return taskListRepository.findByTaskListIdAndPassiveIdIsNull(taskListId)
                .orElseThrow(NotFoundException::new);
    }

    public TaskListDto retrieve(String taskListId) {
        log.info("Retrieve task list has started. taskListId: {}", taskListId);
        return taskListRepository.findByTaskListIdAndPassiveIdIsNull(taskListId)
                .map(taskListDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }
}
