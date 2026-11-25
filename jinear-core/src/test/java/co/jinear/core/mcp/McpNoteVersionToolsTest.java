package co.jinear.core.mcp;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.note.GetNoteVersionTool;
import co.jinear.core.manager.mcp.tool.note.ListNoteVersionsTool;
import co.jinear.core.manager.note.NoteFilterManager;
import co.jinear.core.manager.richtext.RichTextVersionManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.richtext.RichTextVersionDetailDto;
import co.jinear.core.model.dto.richtext.RichTextVersionDto;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.view.McpNoteVersionDetailView;
import co.jinear.core.model.mcp.view.McpNoteVersionEnvelopeView;
import co.jinear.core.model.mcp.view.McpNoteVersionView;
import co.jinear.core.model.mcp.view.McpPageView;
import co.jinear.core.model.request.note.NoteFilterRequest;
import co.jinear.core.model.response.note.NotePaginatedResponse;
import co.jinear.core.model.response.richtext.RichTextVersionPaginatedResponse;
import co.jinear.core.model.response.richtext.RichTextVersionResponse;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

class McpNoteVersionToolsTest {

    private static final ObjectMapper OBJECT_MAPPER = JsonMapper.builderWithJackson2Defaults().build();
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    private static final String RICH_TEXT_ID = "rich-text-1";

    private final NoteFilterManager noteFilterManager = Mockito.mock(NoteFilterManager.class);
    private final RichTextVersionManager richTextVersionManager = Mockito.mock(RichTextVersionManager.class);
    private final ListNoteVersionsTool listTool = new ListNoteVersionsTool(noteFilterManager, richTextVersionManager, new McpViewConverter());
    private final GetNoteVersionTool getTool = new GetNoteVersionTool(noteFilterManager, richTextVersionManager, new McpViewConverter());

    @Test
    void theVersionsOfTheNotesRichTextAreListedForThePage() throws Exception {
        noteIsVisible();
        Mockito.when(richTextVersionManager.retrieveVersions(RICH_TEXT_ID, 2))
                .thenReturn(versionsResponse(version(new RichTextVersionDto())));

        McpToolResult result = listTool.call(context(), args("""
                {"workspaceId": "ws-1", "noteId": "note-1", "page": 2}
                """));

        McpNoteVersionView view = firstListed(result);
        assertThat(view.getVersionId()).isEqualTo("version-1");
        assertThat(view.getEditorAccountId()).isEqualTo("account-1");
        assertThat(view.getEditorUsername()).isEqualTo("cagdas");
        assertThat(view.getCreatedAt()).isNotNull();
        assertThat(view.getUpdatedAt()).isEqualTo(view.getCreatedAt());
    }

    @Test
    void aNoteThatIsNotVisibleListsNoVersions() throws Exception {
        noteIsNotVisible();

        McpToolResult result = listTool.call(context(), args("""
                {"workspaceId": "ws-1", "noteId": "note-1"}
                """));

        assertThat(result.isError()).isTrue();
        assertThat(result.getText()).isEqualTo("mcp.error.note.not-visible");
        Mockito.verify(richTextVersionManager, Mockito.never()).retrieveVersions(anyString(), anyInt());
    }

    @Test
    void oneVersionIsReadWithItsTitleAndBody() throws Exception {
        noteIsVisible();
        RichTextVersionDetailDto detail = version(new RichTextVersionDetailDto());
        detail.setTitle("Old title");
        detail.setHtml("<p>old</p>");
        RichTextVersionResponse response = new RichTextVersionResponse();
        response.setRichTextVersionDetailDto(detail);
        Mockito.when(richTextVersionManager.retrieveVersion(RICH_TEXT_ID, "version-1")).thenReturn(response);

        McpToolResult result = getTool.call(context(), args("""
                {"workspaceId": "ws-1", "noteId": "note-1", "versionId": "version-1"}
                """));

        McpNoteVersionDetailView view = ((McpNoteVersionEnvelopeView) result.getStructuredContent()).getVersion();
        assertThat(view.getVersionId()).isEqualTo("version-1");
        assertThat(view.getTitle()).isEqualTo("Old title");
        assertThat(view.getBody()).isEqualTo("<p>old</p>");
    }

    @Test
    void aNoteThatIsNotVisibleGivesNoVersion() throws Exception {
        noteIsNotVisible();

        McpToolResult result = getTool.call(context(), args("""
                {"workspaceId": "ws-1", "noteId": "note-1", "versionId": "version-1"}
                """));

        assertThat(result.isError()).isTrue();
        assertThat(result.getText()).isEqualTo("mcp.error.note.not-visible");
        Mockito.verify(richTextVersionManager, Mockito.never()).retrieveVersion(anyString(), anyString());
    }

    private void noteIsVisible() {
        NoteDto note = new NoteDto();
        note.setNoteId("note-1");
        note.setRichTextId(RICH_TEXT_ID);
        Mockito.when(noteFilterManager.filter(any(NoteFilterRequest.class))).thenReturn(notesResponse(List.of(note)));
    }

    private void noteIsNotVisible() {
        Mockito.when(noteFilterManager.filter(any(NoteFilterRequest.class))).thenReturn(notesResponse(List.of()));
    }

    private NotePaginatedResponse notesResponse(List<NoteDto> notes) {
        PageImpl<NoteDto> page = new PageImpl<>(notes, PageRequest.of(0, 50), notes.size());
        NotePaginatedResponse response = new NotePaginatedResponse();
        response.setNoteDtoPageDto(new PageDto<>(page));
        return response;
    }

    private RichTextVersionPaginatedResponse versionsResponse(RichTextVersionDto version) {
        PageImpl<RichTextVersionDto> page = new PageImpl<>(List.of(version), PageRequest.of(2, 50), 101);
        RichTextVersionPaginatedResponse response = new RichTextVersionPaginatedResponse();
        response.setRichTextVersionDtoPageDto(new PageDto<>(page));
        return response;
    }

    private <T extends RichTextVersionDto> T version(T version) {
        PlainAccountProfileDto account = new PlainAccountProfileDto();
        account.setUsername("cagdas");
        version.setRichTextVersionId("version-1");
        version.setRichTextId(RICH_TEXT_ID);
        version.setAccountId("account-1");
        version.setAccount(account);
        version.setCreatedDate(new Date());
        return version;
    }

    @SuppressWarnings("unchecked")
    private McpNoteVersionView firstListed(McpToolResult result) {
        McpPageView<McpNoteVersionView> view = (McpPageView<McpNoteVersionView>) result.getStructuredContent();
        return view.getItems().get(0);
    }

    private McpToolArguments args(String json) throws Exception {
        return McpToolArguments.of(OBJECT_MAPPER.readTree(json), OBJECT_MAPPER, VALIDATOR);
    }

    private McpToolContext context() {
        return McpToolContext.builder().accountId("account-1").scopes(Set.of()).build();
    }
}
