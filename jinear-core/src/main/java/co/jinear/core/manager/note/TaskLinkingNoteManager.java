package co.jinear.core.manager.note;

import co.jinear.core.converter.note.TaskLinkingNoteDtoConverter;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.note.TaskLinkingNoteDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.response.note.TaskLinkingNoteListResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.note.NoteRetrieveService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.validator.note.NoteAccessValidator;
import co.jinear.core.validator.task.TaskTeamVisibilityTypeAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskLinkingNoteManager {

    private final SessionInfoService sessionInfoService;
    private final TaskRetrieveService taskRetrieveService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamMemberRetrieveService teamMemberRetrieveService;
    private final TaskTeamVisibilityTypeAccessValidator teamVisibilityTypeAccessValidator;
    private final NoteRetrieveService noteRetrieveService;
    private final NoteAccessValidator noteAccessValidator;
    private final TaskLinkingNoteDtoConverter taskLinkingNoteDtoConverter;

    public TaskLinkingNoteListResponse retrieveNotesLinkingTask(String taskId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TaskDto taskDto = taskRetrieveService.retrievePlain(taskId);
        validateTaskAccess(currentAccountId, taskDto);
        log.info("Retrieve notes linking task has started. currentAccountId: {}, taskId: {}", currentAccountId, taskId);
        List<NoteDto> linkingNotes = noteRetrieveService.retrieveAllLinkingTask(taskId, taskDto.getWorkspaceId());
        List<NoteDto> readableNotes = noteAccessValidator.filterReadable(currentAccountId, taskDto.getWorkspaceId(), linkingNotes);
        return mapResponse(readableNotes);
    }

    private void validateTaskAccess(String currentAccountId, TaskDto taskDto) {
        workspaceValidator.validateHasAccess(currentAccountId, taskDto.getWorkspaceId());
        TeamMemberDto teamMemberDto = teamMemberRetrieveService.retrieveMembership(taskDto.getWorkspaceId(), taskDto.getTeamId(), currentAccountId)
                .orElseThrow(NoAccessException::new);
        teamVisibilityTypeAccessValidator.validateTaskAccess(currentAccountId, teamMemberDto, taskDto.getOwnerId(), taskDto.getAssignedTo());
    }

    private TaskLinkingNoteListResponse mapResponse(List<NoteDto> noteDtos) {
        List<TaskLinkingNoteDto> taskLinkingNoteDtos = noteDtos.stream()
                .map(taskLinkingNoteDtoConverter::convert)
                .toList();
        TaskLinkingNoteListResponse taskLinkingNoteListResponse = new TaskLinkingNoteListResponse();
        taskLinkingNoteListResponse.setTaskLinkingNoteDtos(taskLinkingNoteDtos);
        return taskLinkingNoteListResponse;
    }
}
