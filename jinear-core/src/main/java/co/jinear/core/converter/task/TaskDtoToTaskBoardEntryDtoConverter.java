package co.jinear.core.converter.task;

import co.jinear.core.model.dto.task.PlainTaskBoardEntryDto;
import co.jinear.core.model.dto.task.TaskBoardEntryDto;
import co.jinear.core.model.dto.task.TaskDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class TaskDtoToTaskBoardEntryDtoConverter {

    public Page<TaskBoardEntryDto> convert(Page<TaskDto> taskDtoPage, String boardId) {
        return taskDtoPage.map(taskDto -> generateTaskBoardEntryFromTaskDto(boardId, taskDto));
    }

    private TaskBoardEntryDto generateTaskBoardEntryFromTaskDto(String boardId, TaskDto taskDto) {
        return Optional.of(taskDto)
                .map(TaskDto::getTaskBoardEntries)
                .map(plainTaskBoardEntryDtos -> getFirstPlainTaskBoardEntryWithTaskBoardIdAndMapToTaskBoardEntryDto(boardId, taskDto, plainTaskBoardEntryDtos))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .orElse(null);
    }

    private Optional<TaskBoardEntryDto> getFirstPlainTaskBoardEntryWithTaskBoardIdAndMapToTaskBoardEntryDto(String boardId, TaskDto taskDto, Set<PlainTaskBoardEntryDto> plainTaskBoardEntryDtos) {
        return plainTaskBoardEntryDtos
                .stream()
                .filter(plainTaskBoardEntryDto -> plainTaskBoardEntryDto.getTaskBoardId().equals(boardId))
                .map(plainTaskBoardEntryDto -> convert(taskDto, plainTaskBoardEntryDto))
                .findFirst();
    }

    private TaskBoardEntryDto convert(TaskDto taskDto, PlainTaskBoardEntryDto plainTaskBoardEntryDto) {
        TaskBoardEntryDto taskBoardEntryDto = new TaskBoardEntryDto();
        taskBoardEntryDto.setTaskBoardEntryId(plainTaskBoardEntryDto.getTaskBoardEntryId());
        taskBoardEntryDto.setTaskId(plainTaskBoardEntryDto.getTaskId());
        taskBoardEntryDto.setTaskBoardId(plainTaskBoardEntryDto.getTaskBoardId());
        taskBoardEntryDto.setOrder(plainTaskBoardEntryDto.getOrder());
        taskBoardEntryDto.setTask(taskDto);
        return taskBoardEntryDto;
    }
}
