package co.jinear.core.converter.task;

import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.entity.task.Task;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.service.media.MediaRetrieveService;
import co.jinear.core.service.richtext.RichTextRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskDtoDetailedConverter {

    private final TaskDtoConverter taskDtoConverter;
    private final MediaRetrieveService mediaRetrieveService;
    private final RichTextRetrieveService richTextRetrieveService;

    public TaskDto map(Task task) {
        return taskDtoConverter.map(task);
    }

    public TaskDto mapAndRetrieveProfilePictures(Task task) {
        log.info("Map and retrieve profile pictures has started.");
        return Optional.of(task)
                .map(taskDtoConverter::map)
                .map(this::retrieveAndSetOwnerProfilePicture)
                .map(this::retrieveAndSetAssigneeProfilePicture)
                .orElse(null);
    }

    public TaskDto mapAndRetrieveProfilePicturesAndTaskDetail(Task task) {
        TaskDto taskDto = mapAndRetrieveProfilePictures(task);
        return Optional.ofNullable(taskDto)
                .map(this::fillRichTextDto)
                .orElse(null);
    }

    private TaskDto retrieveAndSetOwnerProfilePicture(TaskDto taskDto) {
        PlainAccountProfileDto owner = taskDto.getOwner();
        if (Objects.nonNull(owner)) {
            mediaRetrieveService.retrieveProfilePictureOptional(taskDto.getOwnerId()).ifPresent(owner::setProfilePicture);
        }
        return taskDto;
    }

    private TaskDto retrieveAndSetAssigneeProfilePicture(TaskDto taskDto) {
        PlainAccountProfileDto account = taskDto.getAssignedToAccount();
        if (Objects.nonNull(account)) {
            mediaRetrieveService.retrieveProfilePictureOptional(taskDto.getOwnerId()).ifPresent(account::setProfilePicture);
        }
        return taskDto;
    }

    private TaskDto fillRichTextDto(TaskDto taskDto) {
        log.info("Retrieve rich text dto has started.");
        richTextRetrieveService.retrieveByRelatedObject(taskDto.getTaskId(), RichTextType.TASK_DETAIL)
                .ifPresent(taskDto::setDescription);
        return taskDto;
    }
}
