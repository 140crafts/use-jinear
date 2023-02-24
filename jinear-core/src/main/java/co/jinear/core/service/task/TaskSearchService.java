package co.jinear.core.service.task;

import co.jinear.core.converter.task.TaskDtoConverter;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.vo.task.TaskSearchVo;
import co.jinear.core.repository.TaskFtsSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskSearchService {

    private static final int PAGE_SIZE = 50;

    private final TaskFtsSearchRepository taskFtsSearchRepository;
    private final TaskDtoConverter taskDtoConverter;

    public Page<TaskDto> searchTasks(TaskSearchVo taskSearchVo) {
        log.info("Searching tasks has started. {}", taskSearchVo);
        return taskFtsSearchRepository.searchAllTasks(taskSearchVo.getTitle(), taskSearchVo.getWorkspaceId(), taskSearchVo.getTeamId(), PageRequest.of(taskSearchVo.getPage(), PAGE_SIZE))
                .map(taskDtoConverter::map);
    }
}