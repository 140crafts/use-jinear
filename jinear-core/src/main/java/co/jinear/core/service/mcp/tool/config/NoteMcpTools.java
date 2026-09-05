package co.jinear.core.service.mcp.tool.config;

import co.jinear.core.manager.note.NoteFilterManager;
import co.jinear.core.manager.notebook.NotebookListingManager;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpJsonSchema;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.request.note.NoteFilterRequest;
import co.jinear.core.service.mcp.tool.McpShapes;
import co.jinear.core.service.mcp.tool.McpTool;
import co.jinear.core.service.mcp.tool.McpToolArguments;
import co.jinear.core.service.mcp.tool.SimpleMcpTool;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class NoteMcpTools {

    private final NotebookListingManager notebookListingManager;
    private final NoteFilterManager noteFilterManager;

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
                .output(McpShapes.pageSchema("Notebooks in this workspace.", McpShapes.notebookSchema()))
                .readOnly()
                .scopes(OauthScope.NOTES_READ)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    String workspaceId = args.requiredString("workspaceId");
                    context.setWorkspaceId(workspaceId);
                    var page = notebookListingManager.listWorkspaceNotebooks(workspaceId, args.page())
                            .getNotebookDtoPageDto();
                    return McpToolResult.of(McpShapes.page(page, McpShapes::notebook));
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
                .output(McpShapes.pageSchema("Matching notes.", McpShapes.noteSchema()))
                .readOnly()
                .scopes(OauthScope.NOTES_READ)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    NoteFilterRequest request = new NoteFilterRequest();
                    request.setWorkspaceId(args.requiredString("workspaceId"));
                    request.setNotebookId(args.optionalString("notebookId", null));
                    request.setParentNoteId(args.optionalString("parentNoteId", null));
                    request.setPage(args.page());
                    context.setWorkspaceId(request.getWorkspaceId());

                    var page = noteFilterManager.filter(request).getNoteDtoPageDto();
                    String titleContains = args.optionalString("titleContains", null);
                    if (Objects.isNull(titleContains) || titleContains.isBlank()) {
                        return McpToolResult.of(McpShapes.page(page, McpShapes::note));
                    }
                    String needle = titleContains.toLowerCase(Locale.ROOT);
                    var matched = page.getContent().stream()
                            .filter(note -> Objects.nonNull(note.getTitle())
                                    && note.getTitle().toLowerCase(Locale.ROOT).contains(needle))
                            .toList();
                    var result = McpShapes.list(matched, McpShapes::note);
                    result.put("page", page.getNumber());
                    result.put("hasNext", page.isHasNext());
                    return McpToolResult.of(result);
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
                .output(McpShapes.singleSchema("note", "The note, with its body.", McpShapes.noteSchema()))
                .readOnly()
                .scopes(OauthScope.NOTES_READ)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    NoteFilterRequest request = new NoteFilterRequest();
                    request.setWorkspaceId(args.requiredString("workspaceId"));
                    request.setNoteId(args.requiredString("noteId"));
                    context.setWorkspaceId(request.getWorkspaceId());
                    var page = noteFilterManager.filter(request).getNoteDtoPageDto();
                    if (page.getContent().isEmpty()) {
                        return McpToolResult.error("No note with that id is visible to you in this workspace. "
                                + "Check noteId against search_notes.");
                    }
                    return McpToolResult.of(McpShapes.single("note", McpShapes.noteDetail(page.getContent().get(0))));
                })
                .build();
    }
}
