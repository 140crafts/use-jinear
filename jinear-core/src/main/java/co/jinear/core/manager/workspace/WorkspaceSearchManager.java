package co.jinear.core.manager.workspace;

import co.jinear.core.manager.material.MaterialListingManager;
import co.jinear.core.manager.note.NoteSearchManager;
import co.jinear.core.manager.task.TaskSearchManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.workspace.WorkspaceSearchDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceSearchType;
import co.jinear.core.model.request.material.MaterialTextSearchRequest;
import co.jinear.core.model.request.note.NoteSearchRequest;
import co.jinear.core.model.request.task.TaskSearchRequest;
import co.jinear.core.model.request.workspace.WorkspaceSearchRequest;
import co.jinear.core.model.response.workspace.WorkspaceSearchResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceSearchManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final TaskSearchManager taskSearchManager;
    private final NoteSearchManager noteSearchManager;
    private final MaterialListingManager materialListingManager;

    public WorkspaceSearchResponse search(WorkspaceSearchRequest request, int page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String workspaceId = request.getWorkspaceId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        Set<WorkspaceSearchType> types = requestedTypes(request);
        log.info("Workspace search has started. accountId: {}, workspaceId: {}, types: {}, page: {}", currentAccountId, workspaceId, types, page);

        WorkspaceSearchDto workspaceSearchDto = new WorkspaceSearchDto();
        if (types.contains(WorkspaceSearchType.TASK)) {
            populateTasks(workspaceSearchDto, request, page);
        }
        if (types.contains(WorkspaceSearchType.NOTE)) {
            populateNotes(workspaceSearchDto, request, page);
        }
        if (types.contains(WorkspaceSearchType.MATERIAL)) {
            populateMaterials(workspaceSearchDto, request, page);
        }
        return mapResponse(workspaceSearchDto);
    }

    private Set<WorkspaceSearchType> requestedTypes(WorkspaceSearchRequest request) {
        if (Objects.isNull(request.getTypes()) || request.getTypes().isEmpty()) {
            return EnumSet.allOf(WorkspaceSearchType.class);
        }
        return EnumSet.copyOf(request.getTypes());
    }

    private void populateTasks(WorkspaceSearchDto workspaceSearchDto, WorkspaceSearchRequest request, int page) {
        TaskSearchRequest taskSearchRequest = new TaskSearchRequest();
        taskSearchRequest.setWorkspaceId(request.getWorkspaceId());
        taskSearchRequest.setTeamIdList(request.getTeamIdList());
        taskSearchRequest.setQuery(request.getQuery());
        PageDto<TaskDto> tasks = taskSearchManager.searchTask(taskSearchRequest, page).getResult();
        workspaceSearchDto.setTasks(tasks);
    }

    private void populateNotes(WorkspaceSearchDto workspaceSearchDto, WorkspaceSearchRequest request, int page) {
        NoteSearchRequest noteSearchRequest = new NoteSearchRequest();
        noteSearchRequest.setQuery(request.getQuery());
        PageDto<NoteDto> notes = noteSearchManager.searchNotes(request.getWorkspaceId(), noteSearchRequest, page).getNoteDtoPageDto();
        workspaceSearchDto.setNotes(notes);
    }

    private void populateMaterials(WorkspaceSearchDto workspaceSearchDto, WorkspaceSearchRequest request, int page) {
        MaterialTextSearchRequest materialTextSearchRequest = new MaterialTextSearchRequest();
        materialTextSearchRequest.setQuery(request.getQuery());
        PageDto<MaterialDto> materials = materialListingManager.searchText(request.getWorkspaceId(), materialTextSearchRequest, page).getMaterialDtoPageDto();
        workspaceSearchDto.setMaterials(materials);
    }

    private WorkspaceSearchResponse mapResponse(WorkspaceSearchDto workspaceSearchDto) {
        WorkspaceSearchResponse workspaceSearchResponse = new WorkspaceSearchResponse();
        workspaceSearchResponse.setWorkspaceSearchDto(workspaceSearchDto);
        return workspaceSearchResponse;
    }
}
