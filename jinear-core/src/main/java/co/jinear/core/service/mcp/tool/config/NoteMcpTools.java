package co.jinear.core.service.mcp.tool.config;

import co.jinear.core.manager.note.NoteFilterManager;
import co.jinear.core.manager.notebook.NotebookListingManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpJsonSchema;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.request.note.NoteFilterRequest;
import co.jinear.core.service.mcp.tool.McpTool;
import co.jinear.core.service.mcp.tool.SimpleMcpTool;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.Objects;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.notebook.NotebookDto;
import java.util.List;
import co.jinear.core.model.mcp.view.McpNoteDetailView;
import co.jinear.core.model.mcp.view.McpNoteEnvelopeView;
import co.jinear.core.model.mcp.view.McpNoteView;
import co.jinear.core.model.mcp.view.McpNotebookView;
import co.jinear.core.model.mcp.view.McpPageView;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.converter.mcp.McpViewConverter;

@Configuration
@RequiredArgsConstructor
public class NoteMcpTools {

    private final NotebookListingManager notebookListingManager;
    private final NoteFilterManager noteFilterManager;
    private final McpViewConverter mcpViewConverter;

    @Bean
    public McpTool listNotebooksTool() {
        return SimpleMcpTool.named("list_notebooks")
                .title("List notebooks")
                .description("Lists the notebooks in a workspace. A notebook groups related notes, "
                        + "and its id is what narrows search_notes to one place.")
                .input(McpJsonSchema.object()
                        .requiredString("workspaceId", "Workspace id, from list_workspaces.")
                        .integer("page", "Zero based page number. Defaults to 0.")
                        .build())
                .output(McpSchemaGenerator.page(McpNotebookView.class, "Notebooks in this workspace."))
                .readOnly()
                .scopes(OauthScope.NOTES_READ)
                .handler((context, args) -> {
                    String workspaceId = args.requiredString("workspaceId");
                    context.setWorkspaceId(workspaceId);
                    PageDto<NotebookDto> page = notebookListingManager.listWorkspaceNotebooks(workspaceId, args.page())
                            .getNotebookDtoPageDto();
                    return McpToolResult.of(McpPageView.of(page, mcpViewConverter::notebook));
                })
                .build();
    }

    @Bean
    public McpTool searchNotesTool() {
        return SimpleMcpTool.named("search_notes")
                .title("Search notes by title")
                .description("Lists notes in a workspace, optionally inside one notebook or under one parent note, "
                        + "and optionally narrowed to titles containing a phrase. "
                        + "Returns titles and ids; call get_note for a note's body.")
                .input(McpJsonSchema.object()
                        .requiredString("workspaceId", "Workspace id, from list_workspaces.")
                        .string("notebookId", "Restrict to one notebook, from list_notebooks.")
                        .string("parentNoteId", "Restrict to the children of one note.")
                        .string("titleContains", "Case insensitive phrase to match against note titles.")
                        .integer("page", "Zero based page number. Defaults to 0.")
                        .build())
                .output(McpSchemaGenerator.page(McpNoteView.class, "Matching notes."))
                .readOnly()
                .scopes(OauthScope.NOTES_READ)
                .handler((context, args) -> {
                    NoteFilterRequest request = new NoteFilterRequest();
                    request.setWorkspaceId(args.requiredString("workspaceId"));
                    request.setNotebookId(args.optionalString("notebookId", null));
                    request.setParentNoteId(args.optionalString("parentNoteId", null));
                    request.setPage(args.page());
                    context.setWorkspaceId(request.getWorkspaceId());

                    PageDto<NoteDto> page = noteFilterManager.filter(request).getNoteDtoPageDto();
                    String titleContains = args.optionalString("titleContains", null);
                    if (Objects.isNull(titleContains) || titleContains.isBlank()) {
                        return McpToolResult.of(McpPageView.of(page, mcpViewConverter::note));
                    }
                    String needle = titleContains.toLowerCase(Locale.ROOT);
                    List<McpNoteView> matched = page.getContent().stream()
                            .filter(note -> Objects.nonNull(note.getTitle())
                                    && note.getTitle().toLowerCase(Locale.ROOT).contains(needle))
                            .map(mcpViewConverter::note)
                            .toList();
                    return McpToolResult.of(McpPageView.of(page, matched));
                })
                .build();
    }

    @Bean
    public McpTool getNoteTool() {
        return SimpleMcpTool.named("get_note")
                .title("Get one note")
                .description("Reads a single note in full, including its body. "
                        + "Use it after search_notes when the contents of a specific note are needed.")
                .input(McpJsonSchema.object()
                        .requiredString("workspaceId", "Workspace id, from list_workspaces.")
                        .requiredString("noteId", "Note id, from search_notes.")
                        .build())
                .output(McpSchemaGenerator.single("note", "The note, with its body.", McpNoteDetailView.class))
                .readOnly()
                .scopes(OauthScope.NOTES_READ)
                .handler((context, args) -> {
                    NoteFilterRequest request = new NoteFilterRequest();
                    request.setWorkspaceId(args.requiredString("workspaceId"));
                    request.setNoteId(args.requiredString("noteId"));
                    context.setWorkspaceId(request.getWorkspaceId());
                    PageDto<NoteDto> page = noteFilterManager.filter(request).getNoteDtoPageDto();
                    if (page.getContent().isEmpty()) {
                        return McpToolResult.error("No note with that id is visible to you in this workspace. "
                                + "Check noteId against search_notes.");
                    }
                    return McpToolResult.of(McpNoteEnvelopeView.of(mcpViewConverter.noteDetail(page.getContent().get(0))));
                })
                .build();
    }
}
