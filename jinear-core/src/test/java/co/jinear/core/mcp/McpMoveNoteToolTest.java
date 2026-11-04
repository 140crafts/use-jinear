package co.jinear.core.mcp;

import co.jinear.core.converter.mcp.McpNoteEditConverter;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.note.MoveNoteTool;
import co.jinear.core.manager.note.NoteFilterManager;
import co.jinear.core.manager.note.NoteOperationManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.view.McpNoteMoveView;
import co.jinear.core.model.request.note.NoteFilterRequest;
import co.jinear.core.model.request.note.NoteMoveRequest;
import co.jinear.core.model.response.note.NotePaginatedResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class McpMoveNoteToolTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    private final NoteOperationManager noteOperationManager = Mockito.mock(NoteOperationManager.class);
    private final NoteFilterManager noteFilterManager = Mockito.mock(NoteFilterManager.class);
    private final MoveNoteTool tool = new MoveNoteTool(noteOperationManager, noteFilterManager, new McpNoteEditConverter());

    @Test
    void theMoveReturnsTheLinkToTheNoteAtItsNewPlace() throws Exception {
        Mockito.when(noteFilterManager.filter(any(NoteFilterRequest.class)))
                .thenReturn(response("https://app.jinear.co/acme/notebook/nb-2/note/note-1"));

        McpToolResult result = tool.call(context(), args("""
                {"workspaceId": "ws-1", "noteId": "note-1", "notebookId": "nb-2"}
                """));

        InOrder order = Mockito.inOrder(noteOperationManager, noteFilterManager);
        order.verify(noteOperationManager).changeNotebook("ws-1", "note-1", "nb-2");
        order.verify(noteOperationManager).move(Mockito.eq("ws-1"), any(NoteMoveRequest.class));
        ArgumentCaptor<NoteFilterRequest> captor = ArgumentCaptor.forClass(NoteFilterRequest.class);
        order.verify(noteFilterManager).filter(captor.capture());
        assertThat(captor.getValue().getNoteId()).isEqualTo("note-1");
        McpNoteMoveView view = (McpNoteMoveView) result.getStructuredContent();
        assertThat(view.getNoteId()).isEqualTo("note-1");
        assertThat(view.getUrl()).isEqualTo("https://app.jinear.co/acme/notebook/nb-2/note/note-1");
        assertThat(view.getOk()).isTrue();
    }

    private McpToolArguments args(String json) throws Exception {
        return McpToolArguments.of(OBJECT_MAPPER.readTree(json), OBJECT_MAPPER, VALIDATOR);
    }

    private McpToolContext context() {
        return McpToolContext.builder().accountId("account-1").scopes(Set.of()).build();
    }

    private NotePaginatedResponse response(String url) {
        NoteDto note = new NoteDto();
        note.setNoteId("note-1");
        note.setUrl(url);
        PageImpl<NoteDto> page = new PageImpl<>(List.of(note), PageRequest.of(0, 50), 1);
        NotePaginatedResponse response = new NotePaginatedResponse();
        response.setNoteDtoPageDto(new PageDto<>(page));
        return response;
    }
}
