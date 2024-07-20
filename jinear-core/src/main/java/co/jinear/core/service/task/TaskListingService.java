package co.jinear.core.service.task;

import co.jinear.core.converter.task.TaskDtoDetailedConverter;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.vo.task.TaskSearchFilterVo;
import co.jinear.core.repository.TaskSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskListingService {

    private final TaskSearchRepository taskSearchRepository;
    private final TaskDtoDetailedConverter taskDtoDetailedConverter;

    public Page<TaskDto> filterTasks(TaskSearchFilterVo taskSearchFilterVo) {
        log.info("Filter tasks has started. taskSearchFilterVo: {}", taskSearchFilterVo);
        return taskSearchRepository.filterBy(taskSearchFilterVo)
                .map(taskDtoDetailedConverter::map);
    }
}
