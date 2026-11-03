package co.jinear.core.mcp;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.note.SearchNotesTool;
import co.jinear.core.manager.note.NoteFilterManager;
import co.jinear.core.manager.note.NoteSearchManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.view.McpNoteView;
import co.jinear.core.model.mcp.view.McpPageView;
import co.jinear.core.model.request.note.NoteFilterRequest;
import co.jinear.core.model.request.note.NoteSearchRequest;
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

class McpSearchNotesToolTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    private final NoteFilterManager noteFilterManager = Mockito.mock(NoteFilterManager.class);
    private final NoteSearchManager noteSearchManager = Mockito.mock(NoteSearchManager.class);
    private final SearchNotesTool tool = new SearchNotesTool(noteFilterManager, noteSearchManager, new McpViewConverter());

    @Test
    void aQueryGoesToTheFullTextSearchWithItsFilters() throws Exception {
        Mockito.when(noteSearchManager.searchNotes(anyString(), any(NoteSearchRequest.class), anyInt()))
                .thenReturn(response("note-1", "Release plan"));

        McpToolResult result = tool.call(context(), args("""
                {"workspaceId": "ws-1", "query": "release pla", "notebookId": "nb-1", "parentNoteId": "parent-1", "page": 2}
                """));

        ArgumentCaptor<NoteSearchRequest> captor = ArgumentCaptor.forClass(NoteSearchRequest.class);
        Mockito.verify(noteSearchManager).searchNotes(Mockito.eq("ws-1"), captor.capture(), Mockito.eq(2));
        NoteSearchRequest request = captor.getValue();
        assertThat(request.getQuery()).isEqualTo("release pla");
        assertThat(request.getNotebookId()).isEqualTo("nb-1");
        assertThat(request.getParentNoteId()).isEqualTo("parent-1");
        Mockito.verifyNoInteractions(noteFilterManager);
        assertThat(titles(result)).containsExactly("Release plan");
    }

    @Test
    void withoutAQueryTheNotesAreListed() throws Exception {
        Mockito.when(noteFilterManager.filter(any(NoteFilterRequest.class)))
                .thenReturn(response("note-2", "Draft"));

        McpToolResult result = tool.call(context(), args("""
                {"workspaceId": "ws-1", "query": "  ", "notebookId": "nb-1"}
                """));

        ArgumentCaptor<NoteFilterRequest> captor = ArgumentCaptor.forClass(NoteFilterRequest.class);
        Mockito.verify(noteFilterManager).filter(captor.capture());
        NoteFilterRequest request = captor.getValue();
        assertThat(request.getWorkspaceId()).isEqualTo("ws-1");
        assertThat(request.getNotebookId()).isEqualTo("nb-1");
        assertThat(request.getPage()).isZero();
        Mockito.verifyNoInteractions(noteSearchManager);
        assertThat(titles(result)).containsExactly("Draft");
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
