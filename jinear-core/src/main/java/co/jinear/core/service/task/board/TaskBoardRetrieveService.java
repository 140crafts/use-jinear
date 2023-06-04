package co.jinear.core.service.task.board;

import co.jinear.core.converter.task.TaskBoardDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.task.TaskBoardDto;
import co.jinear.core.model.entity.task.TaskBoard;
import co.jinear.core.repository.TaskBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskBoardRetrieveService {

    private final TaskBoardRepository taskBoardRepository;
    private final TaskBoardDtoConverter taskBoardDtoConverter;

    public TaskBoard retrieveEntity(String taskBoardId) {
        log.info("Retrieve task board entity has started. taskBoardId: {}", taskBoardId);
        return taskBoardRepository.findByTaskBoardIdAndPassiveIdIsNull(taskBoardId)
                .orElseThrow(NotFoundException::new);
    }

    public TaskBoardDto retrieve(String taskBoardId) {
        log.info("Retrieve task board has started. taskBoardId: {}", taskBoardId);
        return taskBoardRepository.findByTaskBoardIdAndPassiveIdIsNull(taskBoardId)
                .map(taskBoardDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }
}
