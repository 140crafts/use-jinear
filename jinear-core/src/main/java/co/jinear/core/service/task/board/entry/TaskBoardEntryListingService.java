package co.jinear.core.service.task.board.entry;

import co.jinear.core.converter.task.TaskBoardEntryDtoConverter;
import co.jinear.core.model.dto.task.TaskBoardEntryDetailedDto;
import co.jinear.core.model.dto.task.TaskBoardEntryDto;
import co.jinear.core.repository.TaskBoardEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskBoardEntryListingService {

    private static final int PAGE_SIZE = 500;

    private final TaskBoardEntryRepository taskBoardEntryRepository;
    private final TaskBoardEntryDtoConverter taskBoardEntryDtoConverter;

    public Page<TaskBoardEntryDto> retrieveTaskBoardEntries(String taskBoardId, int page) {
        log.info("Retrieve task board entries from task list has started. taskBoardId: {}, page: {}", taskBoardId, page);
        return taskBoardEntryRepository.findAllByTaskBoardIdAndPassiveIdIsNullOrderByOrder(taskBoardId, PageRequest.of(page, PAGE_SIZE))
                .map(taskBoardEntryDtoConverter::map);
    }

    public List<TaskBoardEntryDetailedDto> retrieveAllEntriesRelatedWithTask(String taskId) {
        log.info("Retrieve all board entries with taskId has started. taskId: {}", taskId);
        return taskBoardEntryRepository.findAllByTaskIdAndPassiveIdIsNullOrderByCreatedDateDesc(taskId, PageRequest.of(0, PAGE_SIZE))
                .stream()
                .map(taskBoardEntryDtoConverter::mapDetailed)
                .toList();
    }
}
