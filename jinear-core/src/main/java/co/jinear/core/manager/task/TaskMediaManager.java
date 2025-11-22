package co.jinear.core.manager.task;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.media.AccessibleMediaDto;
import co.jinear.core.model.dto.media.MediaDto;
import co.jinear.core.model.dto.media.WaitingMediaResultDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.task.TaskMediaDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.enumtype.media.MediaFileUploadStatusType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import co.jinear.core.model.enumtype.workspace.WorkspaceTier;
import co.jinear.core.model.request.media.MediaUploadUrlRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.media.MediaUploadUrlResponse;
import co.jinear.core.model.response.task.TaskMediaResponse;
import co.jinear.core.model.response.task.TaskPaginatedMediaResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.media.MediaRetrieveService;
import co.jinear.core.service.task.TaskActivityService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.task.TaskSearchService;
import co.jinear.core.service.task.media.TaskMediaOperationService;
import co.jinear.core.service.task.media.TaskMediaRetrieveService;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.validator.task.TaskAccessValidator;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceMediaLimitValidator;
import co.jinear.core.validator.workspace.WorkspaceTierValidator;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final TeamAccessValidator teamAccessValidator;
    private final TeamRetrieveService teamRetrieveService;
    private final TaskSearchService taskSearchService;
    private final MediaRetrieveService mediaRetrieveService;

    public TaskMediaResponse retrieveTaskMediaList(String taskId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TaskDto taskDto = taskRetrieveService.retrievePlain(taskId);
        taskAccessValidator.validateTaskAccess(currentAccountId, taskDto);
        log.info("Retrieve task media list has started. currentAccountId: {}", currentAccountId);
        List<MediaDto> taskRelatedMedia = taskMediaRetrieveService.retrieveTaskRelatedMedia(taskId);
        return mapResponse(taskRelatedMedia);
    }

    public TaskPaginatedMediaResponse retrieveTaskMediaListFromTeam(String teamId, Integer page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TeamDto teamDto = teamRetrieveService.retrieveTeam(teamId);
        teamAccessValidator.validateTeamAccess(currentAccountId, teamDto);
        log.info("Retrieve task media list from team has started. accountId: {}, page: {}", currentAccountId, page);
        PageDto<TaskMediaDto> data = taskMediaRetrieveService.retrieveAllFromWorkspaceAndTeam(teamDto.getWorkspaceId(), teamDto.getTeamId(), MediaFileUploadStatusType.COMPLETED, page);
        return mapResponse(data);
    }

    public BaseResponse uploadTaskMedia(String taskId, MultipartFile file) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String currentAccountSessionId = sessionInfoService.currentAccountSessionId();
        TaskDto taskDto = taskRetrieveService.retrievePlain(taskId);
        taskAccessValidator.validateTaskAccess(currentAccountId, taskDto);
        workspaceTierValidator.validateWorkspaceTier(taskDto.getWorkspaceId(), WorkspaceTier.PRO);
        workspaceMediaLimitValidator.validateWorkspaceStorageLimitNotExceeded(taskDto.getWorkspaceId(), file.getSize());
        log.info("Upload task media has started. currentAccountId: {}", currentAccountId);
        AccessibleMediaDto accessibleMediaDto = taskMediaOperationService.upload(currentAccountId, taskDto, file);
        taskActivityService.initializeTaskAttachmentAddedActivity(currentAccountId, currentAccountSessionId, taskDto, accessibleMediaDto.getMediaId());
        taskSearchService.refreshTaskFtsMv();
        return new BaseResponse();
    }

    public BaseResponse deleteTaskMedia(String taskId, String mediaId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String currentAccountSessionId = sessionInfoService.currentAccountSessionId();
        TaskDto taskDto = taskRetrieveService.retrievePlain(taskId);
        taskAccessValidator.validateTaskAccess(currentAccountId, taskDto);
        log.info("Delete task media has started. currentAccountId: {}", currentAccountId);
        AccessibleMediaDto accessibleMediaDto = taskMediaOperationService.delete(currentAccountId, mediaId, taskId);
        taskActivityService.initializeTaskAttachmentDeletedActivity(currentAccountId, currentAccountSessionId, taskDto, accessibleMediaDto);
        taskSearchService.refreshTaskFtsMv();
        return new BaseResponse();
    }

    public void downloadTaskMedia(HttpServletResponse response, String taskId, String mediaId) throws IOException {
        String currentAccountId = sessionInfoService.currentAccountId();
        TaskDto taskDto = taskRetrieveService.retrievePlain(taskId);
        taskAccessValidator.validateTaskAccess(currentAccountId, taskDto);
        AccessibleMediaDto accessibleMediaDto = taskMediaRetrieveService.retrieveAccessible(taskId, mediaId, MediaFileUploadStatusType.COMPLETED);
        String redirectUrl = MediaVisibilityType.PRIVATE.equals(accessibleMediaDto.getVisibility()) ? taskMediaRetrieveService.retrievePublicDownloadLink(accessibleMediaDto) : accessibleMediaDto.getUrl();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"%s\"".formatted(accessibleMediaDto.getOriginalName()));
        response.sendRedirect(redirectUrl);
    }

    public BaseResponse updateTaskMediaVisibility(String taskId, String mediaId, MediaVisibilityType mediaVisibilityType) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TaskDto taskDto = taskRetrieveService.retrievePlain(taskId);
        taskAccessValidator.validateTaskAccess(currentAccountId, taskDto);
        validateMediaRelatedWithTask(taskId, mediaId);
        log.info("Update task media visibility has started. currentAccountId: {}", currentAccountId);
        taskMediaOperationService.updateMediaVisibility(mediaId, mediaVisibilityType);
        return new BaseResponse();
    }

    public MediaUploadUrlResponse retrieveTaskMediaUploadUrl(String taskId, MediaUploadUrlRequest mediaUploadUrlRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String currentAccountSessionId = sessionInfoService.currentAccountSessionId();
        TaskDto taskDto = taskRetrieveService.retrievePlain(taskId);
        taskAccessValidator.validateTaskAccess(currentAccountId, taskDto);
        workspaceTierValidator.validateWorkspaceTier(taskDto.getWorkspaceId(), WorkspaceTier.PRO);
        workspaceMediaLimitValidator.validateWorkspaceStorageLimitNotExceeded(taskDto.getWorkspaceId(), mediaUploadUrlRequest.getFileSize());
        log.info("Retrieve task media upload url has started. currentAccountId: {}", currentAccountId);
        WaitingMediaResultDto waitingMediaResultDto = taskMediaOperationService.retrieveUploadUrl(taskId, taskDto, mediaUploadUrlRequest.getOriginalName(), mediaUploadUrlRequest.getContentType(), mediaUploadUrlRequest.getFileSize());
        return mapResponse(waitingMediaResultDto);
    }

    public BaseResponse notifyUploadCompleted(String taskId, String mediaId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String currentAccountSessionId = sessionInfoService.currentAccountSessionId();
        TaskDto taskDto = taskRetrieveService.retrievePlain(taskId);
        taskAccessValidator.validateTaskAccess(currentAccountId, taskDto);
        validateMediaRelatedWithTask(taskId, mediaId);
        taskMediaOperationService.updateTaskMediaAsCompleted(mediaId);
        taskActivityService.initializeTaskAttachmentAddedActivity(currentAccountId, currentAccountSessionId, taskDto, mediaId);
        taskSearchService.refreshTaskFtsMv();
        return new BaseResponse();
    }

    private void validateMediaRelatedWithTask(String taskId, String mediaId) {
        boolean isRelated = mediaRetrieveService.checkMediaRelatedWithRelatedObjectId(mediaId, taskId);
        if (!isRelated) {
            throw new NoAccessException();
        }
    }

    private TaskMediaResponse mapResponse(List<MediaDto> taskRelatedMedia) {
        TaskMediaResponse taskMediaResponse = new TaskMediaResponse();
        taskMediaResponse.setData(taskRelatedMedia);
        return taskMediaResponse;
    }

    private TaskPaginatedMediaResponse mapResponse(PageDto<TaskMediaDto> data) {
        TaskPaginatedMediaResponse taskPaginatedMediaResponse = new TaskPaginatedMediaResponse();
        taskPaginatedMediaResponse.setData(data);
        return taskPaginatedMediaResponse;
    }

    private MediaUploadUrlResponse mapResponse(WaitingMediaResultDto waitingMediaResultDto) {
        MediaUploadUrlResponse mediaUploadUrlResponse = new MediaUploadUrlResponse();
        mediaUploadUrlResponse.setWaitingMediaResultDto(waitingMediaResultDto);
        return mediaUploadUrlResponse;
    }
}
