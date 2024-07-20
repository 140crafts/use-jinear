package co.jinear.core.converter.task;

import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.entity.task.Task;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.service.richtext.RichTextRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskDtoDetailedConverter {

    private final TaskDtoConverter taskDtoConverter;
    private final RichTextRetrieveService richTextRetrieveService;

    public TaskDto map(Task task) {
        return taskDtoConverter.map(task);
    }

    public TaskDto mapAndRetrieveProfilePicturesAndTaskDetail(Task task) {
        TaskDto taskDto = map(task);
        return Optional.ofNullable(taskDto)
                .map(this::fillRichTextDto)
                .orElse(null);
    }

    private TaskDto fillRichTextDto(TaskDto taskDto) {
        log.info("Retrieve rich text dto has started.");
        richTextRetrieveService.retrieveByRelatedObject(taskDto.getTaskId(), RichTextType.TASK_DETAIL)
                .ifPresent(taskDto::setDescription);
        return taskDto;
    }
}
