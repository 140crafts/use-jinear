package co.jinear.core.mcp;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.note.ListNotesTool;
import co.jinear.core.manager.note.NoteSearchManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.view.McpNoteView;
import co.jinear.core.model.mcp.view.McpPageView;
import co.jinear.core.model.request.note.NoteListRequest;
import co.jinear.core.model.response.note.NotePaginatedResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

class McpListNotesToolTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    private final NoteSearchManager noteSearchManager = Mockito.mock(NoteSearchManager.class);
    private final ListNotesTool tool = new ListNotesTool(noteSearchManager, new McpViewConverter());

    @Test
    void withoutFiltersTheWholeWorkspaceIsListed() throws Exception {
        Mockito.when(noteSearchManager.listNotes(anyString(), any(NoteListRequest.class), anyInt()))
                .thenReturn(response("note-2", "Draft"));

        McpToolResult result = tool.call(context(), args("""
                {"workspaceId": "ws-1"}
                """));

        ArgumentCaptor<NoteListRequest> captor = ArgumentCaptor.forClass(NoteListRequest.class);
        Mockito.verify(noteSearchManager).listNotes(Mockito.eq("ws-1"), captor.capture(), Mockito.eq(0));
        assertThat(captor.getValue().getNotebookId()).isNull();
        assertThat(captor.getValue().getParentNoteId()).isNull();
        assertThat(titles(result)).containsExactly("Draft");
    }

    @Test
    void theFiltersAndThePageArePassedOn() throws Exception {
        Mockito.when(noteSearchManager.listNotes(anyString(), any(NoteListRequest.class), anyInt()))
                .thenReturn(response("note-3", "Child"));

        tool.call(context(), args("""
                {"workspaceId": "ws-1", "notebookId": "nb-1", "parentNoteId": "parent-1", "page": 1}
                """));

        ArgumentCaptor<NoteListRequest> captor = ArgumentCaptor.forClass(NoteListRequest.class);
        Mockito.verify(noteSearchManager).listNotes(Mockito.eq("ws-1"), captor.capture(), Mockito.eq(1));
        assertThat(captor.getValue().getNotebookId()).isEqualTo("nb-1");
        assertThat(captor.getValue().getParentNoteId()).isEqualTo("parent-1");
    }

    private McpToolArguments args(String json) throws Exception {
        return McpToolArguments.of(OBJECT_MAPPER.readTree(json), OBJECT_MAPPER, VALIDATOR);
    }

    private McpToolContext context() {
        return McpToolContext.builder().accountId("account-1").scopes(Set.of()).build();
    }

    private NotePaginatedResponse response(String noteId, String title) {
        NoteDto note = new NoteDto();
        note.setNoteId(noteId);
        note.setTitle(title);
        PageImpl<NoteDto> page = new PageImpl<>(List.of(note), PageRequest.of(0, 50), 1);
        NotePaginatedResponse response = new NotePaginatedResponse();
        response.setNoteDtoPageDto(new PageDto<>(page));
        return response;
    }

    @SuppressWarnings("unchecked")
    private List<String> titles(McpToolResult result) {
        McpPageView<McpNoteView> view = (McpPageView<McpNoteView>) result.getStructuredContent();
        return view.getItems().stream().map(McpNoteView::getTitle).toList();
    }
}
