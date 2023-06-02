package co.jinear.core.service.task.board;

import co.jinear.core.converter.task.InitializeTaskListVoToEntityConverter;
import co.jinear.core.converter.task.TaskBoardDtoConverter;
import co.jinear.core.model.dto.task.TaskBoardDto;
import co.jinear.core.model.entity.task.TaskBoard;
import co.jinear.core.model.vo.task.InitializeTaskBoardVo;
import co.jinear.core.model.vo.task.UpdateTaskBoardDueDateVo;
import co.jinear.core.model.vo.task.UpdateTaskBoardStateVo;
import co.jinear.core.model.vo.task.UpdateTaskBoardTitleVo;
import co.jinear.core.repository.TaskBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskBoardOperationService {

    private final TaskBoardRepository taskBoardRepository;
    private final InitializeTaskListVoToEntityConverter initializeTaskBoardVoToEntityConverter;
    private final TaskBoardDtoConverter taskBoardDtoConverter;
    private final TaskBoardRetrieveService taskBoardRetrieveService;

    public TaskBoardDto initializeTaskBoard(InitializeTaskBoardVo initializeTaskBoardVo) {
        log.info("Initialize task board has started. initializeTaskListVo: {}", initializeTaskBoardVo);
        TaskBoard taskBoard = initializeTaskBoardVoToEntityConverter.map(initializeTaskBoardVo);
        TaskBoard saved = taskBoardRepository.save(taskBoard);
        return taskBoardDtoConverter.convert(saved);
    }

    public TaskBoardDto updateDueDate(UpdateTaskBoardDueDateVo updateTaskBoardDueDateVo) {
        log.info("Update task board due date has started. updateTaskBoardDueDateVo: {}", updateTaskBoardDueDateVo);
        TaskBoard taskBoard = taskBoardRetrieveService.retrieveEntity(updateTaskBoardDueDateVo.getTaskBoardId());
        taskBoard.setDueDate(updateTaskBoardDueDateVo.getDueDate());
        TaskBoard saved = taskBoardRepository.save(taskBoard);
        return taskBoardDtoConverter.convert(saved);
    }

    public TaskBoardDto updateTitle(UpdateTaskBoardTitleVo updateTaskBoardTitleVo) {
        log.info("Update task board title has started. updateTaskBoardTitleVo: {}", updateTaskBoardTitleVo);
        TaskBoard taskBoard = taskBoardRetrieveService.retrieveEntity(updateTaskBoardTitleVo.getTaskBoardId());
        taskBoard.setTitle(updateTaskBoardTitleVo.getTitle());
        TaskBoard saved = taskBoardRepository.save(taskBoard);
        return taskBoardDtoConverter.convert(saved);
    }

    public TaskBoardDto updateState(UpdateTaskBoardStateVo updateTaskBoardStateVo) {
        log.info("Update task board state has started. updateTaskListStateVo: {}", updateTaskBoardStateVo);
        TaskBoard taskBoard = taskBoardRetrieveService.retrieveEntity(updateTaskBoardStateVo.getTaskBoardId());
        taskBoard.setState(updateTaskBoardStateVo.getState());
        TaskBoard saved = taskBoardRepository.save(taskBoard);
        return taskBoardDtoConverter.convert(saved);
    }
}
