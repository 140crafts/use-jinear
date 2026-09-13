package co.jinear.core.mcp;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.converter.mcp.McpLinkConverter;
import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.calendar.CalendarEventManager;
import co.jinear.core.manager.material.MaterialListingManager;
import co.jinear.core.manager.material.MaterialManager;
import co.jinear.core.manager.mcp.tool.McpTool;
import co.jinear.core.manager.mcp.tool.McpToolRegistry;
import co.jinear.core.manager.mcp.tool.board.AddTaskToBoardTool;
import co.jinear.core.manager.mcp.tool.board.CreateTaskBoardTool;
import co.jinear.core.manager.mcp.tool.board.ListTaskBoardsTool;
import co.jinear.core.manager.mcp.tool.board.ListTopicsTool;
import co.jinear.core.manager.mcp.tool.calendar.ListCalendarEventsTool;
import co.jinear.core.manager.mcp.tool.compatibility.FetchTool;
import co.jinear.core.manager.mcp.tool.compatibility.SearchTool;
import co.jinear.core.manager.mcp.tool.file.GetFileLinkTool;
import co.jinear.core.manager.mcp.tool.file.ListFilesTool;
import co.jinear.core.manager.mcp.tool.note.GetNoteTool;
import co.jinear.core.manager.mcp.tool.note.ListNotebooksTool;
import co.jinear.core.manager.mcp.tool.note.SearchNotesTool;
import co.jinear.core.manager.mcp.tool.task.AddTaskCommentTool;
import co.jinear.core.manager.mcp.tool.task.CreateTaskTool;
import co.jinear.core.manager.mcp.tool.task.GetTaskTool;
import co.jinear.core.manager.mcp.tool.task.ListTaskCommentsTool;
import co.jinear.core.manager.mcp.tool.task.ListTasksTool;
import co.jinear.core.manager.mcp.tool.task.SearchTasksTool;
import co.jinear.core.manager.mcp.tool.task.SetTaskStatusTool;
import co.jinear.core.manager.mcp.tool.task.UpdateTaskTool;
import co.jinear.core.manager.mcp.tool.workspace.GetWorkspaceTool;
import co.jinear.core.manager.mcp.tool.workspace.ListTeamsTool;
import co.jinear.core.manager.mcp.tool.workspace.ListWorkflowStatusesTool;
import co.jinear.core.manager.mcp.tool.workspace.ListWorkspaceMembersTool;
import co.jinear.core.manager.mcp.tool.workspace.ListWorkspacesTool;
import co.jinear.core.manager.note.NoteFilterManager;
import co.jinear.core.manager.notebook.NotebookListingManager;
import co.jinear.core.manager.task.TaskBoardEntryManager;
import co.jinear.core.manager.task.TaskBoardListingManager;
import co.jinear.core.manager.task.TaskBoardManager;
import co.jinear.core.manager.task.TaskCommentManager;
import co.jinear.core.manager.task.TaskInitializeManager;
import co.jinear.core.manager.task.TaskListingManager;
import co.jinear.core.manager.task.TaskRetrieveManager;
import co.jinear.core.manager.task.TaskSearchManager;
import co.jinear.core.manager.task.TaskUpdateManager;
import co.jinear.core.manager.team.TeamRetrieveManager;
import co.jinear.core.manager.team.TeamWorkflowStatusManager;
import co.jinear.core.manager.topic.TopicListingManager;
import co.jinear.core.manager.workspace.WorkspaceManager;
import co.jinear.core.manager.workspace.WorkspaceMemberRetrieveManager;
import org.mockito.Mockito;

import java.util.List;

final class McpRealCatalog {

    private McpRealCatalog() {
    }

    static McpProperties properties() {
        McpProperties properties = new McpProperties();
        properties.setEnabled(Boolean.TRUE);
        properties.setResourceUrl("https://api.jinear.co/mcp");
        properties.setDocumentationUrl("https://jinear.co/mcp/");
        properties.setMaxPageSize(50);
        return properties;
    }

    static OauthProperties oauthProperties() {
        OauthProperties properties = new OauthProperties();
        properties.setIssuerUrl("https://api.jinear.co");
        return properties;
    }

    static List<McpTool> tools() {
        McpViewConverter viewConverter = new McpViewConverter();
        FeProperties feProperties = new FeProperties();
        McpLinkConverter linkConverter = new McpLinkConverter(feProperties);

        return List.of(
                new ListWorkspacesTool(mock(WorkspaceManager.class), viewConverter),
                new GetWorkspaceTool(mock(WorkspaceManager.class), viewConverter),
                new ListTeamsTool(mock(TeamRetrieveManager.class), viewConverter),
                new ListWorkflowStatusesTool(mock(TeamWorkflowStatusManager.class), viewConverter),
                new ListWorkspaceMembersTool(mock(WorkspaceMemberRetrieveManager.class), viewConverter),

                new SearchTasksTool(mock(TaskSearchManager.class), viewConverter),
                new ListTasksTool(mock(TaskListingManager.class), properties(), viewConverter),
                new GetTaskTool(mock(TaskRetrieveManager.class), viewConverter),
                new CreateTaskTool(mock(TaskInitializeManager.class), viewConverter),
                new UpdateTaskTool(mock(TaskUpdateManager.class)),
                new SetTaskStatusTool(mock(TaskUpdateManager.class), viewConverter),
                new ListTaskCommentsTool(mock(TaskCommentManager.class), viewConverter),
                new AddTaskCommentTool(mock(TaskCommentManager.class)),

                new ListTaskBoardsTool(mock(TaskBoardListingManager.class), viewConverter),
                new CreateTaskBoardTool(mock(TaskBoardManager.class), viewConverter),
                new AddTaskToBoardTool(mock(TaskBoardEntryManager.class)),
                new ListTopicsTool(mock(TopicListingManager.class), viewConverter),

                new ListCalendarEventsTool(mock(CalendarEventManager.class), viewConverter),

                new ListNotebooksTool(mock(NotebookListingManager.class), viewConverter),
                new SearchNotesTool(mock(NoteFilterManager.class), viewConverter),
                new GetNoteTool(mock(NoteFilterManager.class), viewConverter),

                new ListFilesTool(mock(MaterialListingManager.class), viewConverter),
                new GetFileLinkTool(mock(MaterialManager.class), oauthProperties()),

                new SearchTool(mock(WorkspaceManager.class), mock(TaskSearchManager.class),
                        mock(NoteFilterManager.class), feProperties, linkConverter),
                new FetchTool(mock(NoteFilterManager.class), mock(TaskRetrieveManager.class),
                        feProperties, linkConverter));
    }

    static McpToolRegistry registry() {
        return new McpToolRegistry(tools());
    }

    private static <T> T mock(Class<T> type) {
        return Mockito.mock(type);
    }
}
