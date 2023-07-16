package co.jinear.core.manager.task;

import co.jinear.core.model.dto.media.AccessibleMediaDto;
import co.jinear.core.model.dto.media.MediaDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceTier;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.TaskMediaResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.TaskActivityService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.task.media.TaskMediaOperationService;
import co.jinear.core.service.task.media.TaskMediaRetrieveService;
import co.jinear.core.validator.task.TaskAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceMediaLimitValidator;
import co.jinear.core.validator.workspace.WorkspaceTierValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskMediaManager {

    private final TaskMediaOperationService taskMediaOperationService;
    private final TaskMediaRetrieveService taskMediaRetrieveService;
    private final SessionInfoService sessionInfoService;
    private final TaskRetrieveService taskRetrieveService;
    private final TaskAccessValidator taskAccessValidator;
    private final WorkspaceTierValidator workspaceTierValidator;
    private final WorkspaceMediaLimitValidator workspaceMediaLimitValidator;
    private final TaskActivityService taskActivityService;

    public TaskMediaResponse retrieveTaskMediaList(String taskId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TaskDto taskDto = taskRetrieveService.retrievePlain(taskId);
        taskAccessValidator.validateTaskAccess(currentAccountId, taskDto);
        log.info("Retrieve task media list has started. currentAccountId: {}", currentAccountId);
        List<MediaDto> taskRelatedMedia = taskMediaRetrieveService.retrieveTaskRelatedMedia(taskId);
        return mapResponse(taskRelatedMedia);
    }

    public BaseResponse uploadTaskMedia(String taskId, MultipartFile file) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TaskDto taskDto = taskRetrieveService.retrievePlain(taskId);
        taskAccessValidator.validateTaskAccess(currentAccountId, taskDto);
        workspaceTierValidator.validateWorkspaceTier(taskDto.getWorkspaceId(), WorkspaceTier.PLUS);
        workspaceMediaLimitValidator.validateWorkspaceStorageLimitNotExceeded(taskDto.getWorkspaceId(), file.getSize());
        log.info("Upload task media has started. currentAccountId: {}", currentAccountId);
        AccessibleMediaDto accessibleMediaDto = taskMediaOperationService.upload(currentAccountId, taskDto, file);
        taskActivityService.initializeTaskAttachmentAddedActivity(currentAccountId, taskDto,accessibleMediaDto);
        return new BaseResponse();
    }

    public BaseResponse deleteTaskMedia(String taskId, String mediaId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TaskDto taskDto = taskRetrieveService.retrievePlain(taskId);
        taskAccessValidator.validateTaskAccess(currentAccountId, taskDto);
        log.info("Delete task media has started. currentAccountId: {}", currentAccountId);
        AccessibleMediaDto accessibleMediaDto = taskMediaOperationService.delete(currentAccountId, mediaId, taskId);
        taskActivityService.initializeTaskAttachmentDeletedActivity(currentAccountId, taskDto,accessibleMediaDto);
        return new BaseResponse();
    }

    public String downloadTaskMedia(String taskId, String mediaId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TaskDto taskDto = taskRetrieveService.retrievePlain(taskId);
        taskAccessValidator.validateTaskAccess(currentAccountId, taskDto);
        AccessibleMediaDto accessibleMediaDto = taskMediaRetrieveService.retrieveAccessible(taskId, mediaId);
        taskMediaOperationService.updateMediaAsTemporaryPublic(mediaId);
        return taskMediaRetrieveService.retrievePublicDownloadLink(accessibleMediaDto);
    }

    private TaskMediaResponse mapResponse(List<MediaDto> taskRelatedMedia) {
        TaskMediaResponse taskMediaResponse = new TaskMediaResponse();
        taskMediaResponse.setData(taskRelatedMedia);
        return taskMediaResponse;
    }
}
