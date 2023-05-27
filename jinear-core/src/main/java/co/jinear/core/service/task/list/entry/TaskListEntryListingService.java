package co.jinear.core.service.task.list.entry;

import co.jinear.core.converter.task.TaskListEntryDtoConverter;
import co.jinear.core.model.dto.task.TaskListEntryDto;
import co.jinear.core.repository.TaskListEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskListEntryListingService {

    private static final int PAGE_SIZE = 250;

    private final TaskListEntryRepository taskListEntryRepository;
    private final TaskListEntryDtoConverter taskListEntryDtoConverter;

    public Page<TaskListEntryDto> retrieveTaskListEntries(String taskListId, int page) {
        log.info("Retrieve task list entries from task list has started. taskListId: {}, page: {}", taskListId, page);
        return taskListEntryRepository.findAllByTaskListIdAndPassiveIdIsNullOrderByOrder(taskListId, PageRequest.of(page, PAGE_SIZE))
                .map(taskListEntryDtoConverter::map);
    }
}
