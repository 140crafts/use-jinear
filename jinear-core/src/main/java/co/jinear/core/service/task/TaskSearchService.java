package co.jinear.core.service.task;

import co.jinear.core.converter.task.TaskDtoDetailedConverter;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.entity.task.TaskFts;
import co.jinear.core.model.vo.task.TaskFtsSearchVo;
import co.jinear.core.repository.TaskFtsSearchRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskSearchService {

    private static final int PAGE_SIZE = 50;

    private final TaskFtsSearchRepository taskFtsSearchRepository;
    private final TaskDtoDetailedConverter taskDtoDetailedConverter;

    public Page<TaskDto> search(TaskFtsSearchVo taskFtsSearchVo) {
        Pageable pageable = PageRequest.of(taskFtsSearchVo.getPage(), PAGE_SIZE);
        return taskFtsSearchRepository.search(
                        taskFtsSearchVo.getQuery(),
                        taskFtsSearchVo.getWorkspaceId(),
                        taskFtsSearchVo.getVisibleToAllTeamIds(),
                        taskFtsSearchVo.getOwnerOrAssigneeTeamIds(),
                        taskFtsSearchVo.getAssignedTo(),
                        taskFtsSearchVo.getOwnerId(),
                        pageable)
                .map(TaskFts::getTask)
                .map(taskDtoDetailedConverter::map);
    }

    @Transactional
    public void refreshTaskFtsMv() {
        long start = System.currentTimeMillis();
        log.info("Refresh task fts mv has started.");
        taskFtsSearchRepository.refreshFtsTasksView();
        long duration = (System.currentTimeMillis() - start);
        log.info("Refresh task fts mv has completed. duration: {}", duration);
    }
}