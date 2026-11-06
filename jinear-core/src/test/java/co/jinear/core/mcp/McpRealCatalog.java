package co.jinear.core.mcp;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.converter.mcp.McpContextViewConverter;
import co.jinear.core.converter.mcp.McpLinkConverter;
import co.jinear.core.converter.mcp.McpNoteEditConverter;
import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.converter.richtext.RichTextPlainTextConverter;
import co.jinear.core.manager.account.AccountContextManager;
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
import co.jinear.core.manager.mcp.tool.file.SearchFilesTool;
import co.jinear.core.manager.mcp.tool.note.AppendToNoteTool;
import co.jinear.core.manager.mcp.tool.note.CreateNoteTool;
import co.jinear.core.manager.mcp.tool.note.DeleteNoteTool;
import co.jinear.core.manager.mcp.tool.note.GetNoteTool;
import co.jinear.core.manager.mcp.tool.note.ListNotebooksTool;
import co.jinear.core.manager.mcp.tool.note.ListNotesTool;
import co.jinear.core.manager.mcp.tool.note.MoveNoteTool;
import co.jinear.core.manager.mcp.tool.note.ReplaceInNoteTool;
import co.jinear.core.manager.mcp.tool.note.SearchNotesTool;
import co.jinear.core.manager.mcp.tool.note.UpdateNoteTitleTool;
import co.jinear.core.manager.mcp.tool.task.AddTaskAttachmentTool;
import co.jinear.core.manager.mcp.tool.task.AddTaskCommentTool;
import co.jinear.core.manager.mcp.tool.task.AddTaskReminderTool;
import co.jinear.core.manager.mcp.tool.task.CreateTaskTool;
import co.jinear.core.manager.mcp.tool.task.GetTaskTool;
import co.jinear.core.manager.mcp.tool.task.ListTaskAttachmentsTool;
import co.jinear.core.manager.mcp.tool.task.ListTaskCommentsTool;
import co.jinear.core.manager.mcp.tool.task.ListTaskRemindersTool;
import co.jinear.core.manager.mcp.tool.task.ListTasksTool;
import co.jinear.core.manager.mcp.tool.task.RemoveTaskReminderTool;
import co.jinear.core.manager.mcp.tool.task.SearchTasksTool;
import co.jinear.core.manager.mcp.tool.task.SetTaskStatusTool;
import co.jinear.core.manager.mcp.tool.task.UpdateTaskTool;
import co.jinear.core.manager.mcp.tool.workspace.GetContextTool;
import co.jinear.core.manager.mcp.tool.workspace.GetWorkspaceTool;
import co.jinear.core.manager.mcp.tool.workspace.ListTeamsTool;
import co.jinear.core.manager.mcp.tool.workspace.ListWorkflowStatusesTool;
import co.jinear.core.manager.mcp.tool.workspace.ListWorkspaceMembersTool;
import co.jinear.core.manager.mcp.tool.workspace.ListWorkspacesTool;
import co.jinear.core.manager.note.NoteContentManager;
import co.jinear.core.manager.note.NoteFilterManager;
import co.jinear.core.manager.note.NoteOperationManager;
import co.jinear.core.manager.note.NoteSearchManager;
import co.jinear.core.manager.notebook.NotebookListingManager;
import co.jinear.core.manager.reminder.TaskReminderManager;
import co.jinear.core.manager.task.TaskBoardEntryManager;
import co.jinear.core.manager.task.TaskBoardListingManager;
import co.jinear.core.manager.task.TaskBoardManager;
import co.jinear.core.manager.task.TaskCommentManager;
import co.jinear.core.manager.task.TaskInitializeManager;
import co.jinear.core.manager.task.TaskListingManager;
import co.jinear.core.manager.task.TaskMediaManager;
import co.jinear.core.manager.task.TaskRetrieveManager;
import co.jinear.core.manager.task.TaskSearchManager;
import co.jinear.core.manager.task.TaskUpdateManager;
import co.jinear.core.manager.team.TeamRetrieveManager;
import co.jinear.core.manager.team.TeamWorkflowStatusManager;
import co.jinear.core.manager.topic.TopicListingManager;
import co.jinear.core.manager.workspace.WorkspaceManager;
import co.jinear.core.manager.workspace.WorkspaceMemberRetrieveManager;
import co.jinear.core.converter.mcp.McpActivityViewConverter;
import co.jinear.core.manager.mcp.tool.board.CreateTopicTool;
import co.jinear.core.manager.mcp.tool.board.MoveTaskOnBoardTool;
import co.jinear.core.manager.mcp.tool.board.RemoveTaskFromBoardTool;
import co.jinear.core.manager.mcp.tool.board.UpdateTaskBoardTool;
import co.jinear.core.manager.mcp.tool.checklist.AddChecklistItemTool;
import co.jinear.core.manager.mcp.tool.checklist.CreateTaskChecklistTool;
import co.jinear.core.manager.mcp.tool.checklist.DeleteChecklistItemTool;
import co.jinear.core.manager.mcp.tool.checklist.DeleteTaskChecklistTool;
import co.jinear.core.manager.mcp.tool.checklist.RenameTaskChecklistTool;
import co.jinear.core.manager.mcp.tool.checklist.UpdateChecklistItemTool;
import co.jinear.core.manager.mcp.tool.note.CreateNotebookTool;
import co.jinear.core.manager.mcp.tool.notification.GetUnreadNotificationCountTool;
import co.jinear.core.manager.mcp.tool.notification.ListNotificationsTool;
import co.jinear.core.manager.mcp.tool.task.AddTaskRelationTool;
import co.jinear.core.manager.mcp.tool.task.ListActivityTool;
import co.jinear.core.manager.mcp.tool.task.RemoveTaskRelationTool;
import co.jinear.core.manager.notebook.NotebookOperationManager;
import co.jinear.core.manager.notification.NotificationEventListingManager;
import co.jinear.core.manager.task.TaskChecklistItemManager;
import co.jinear.core.manager.task.TaskChecklistManager;
import co.jinear.core.manager.task.TaskRelationManager;
import co.jinear.core.manager.topic.TopicManager;
import co.jinear.core.manager.workspace.WorkspaceActivityFilterManager;
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
        McpNoteEditConverter noteEditConverter = new McpNoteEditConverter();
        FeProperties feProperties = new FeProperties();
        McpLinkConverter linkConverter = new McpLinkConverter(oauthProperties());

        return List.of(
                new GetContextTool(mock(AccountContextManager.class), new McpContextViewConverter()),
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
                new ListTaskAttachmentsTool(mock(TaskMediaManager.class), viewConverter, linkConverter),
                new AddTaskAttachmentTool(mock(TaskMediaManager.class), linkConverter),
                new ListTaskRemindersTool(mock(TaskReminderManager.class), viewConverter),
                new AddTaskReminderTool(mock(TaskReminderManager.class), viewConverter),
                new RemoveTaskReminderTool(mock(TaskReminderManager.class)),
                new AddTaskRelationTool(mock(TaskRelationManager.class), viewConverter),
                new RemoveTaskRelationTool(mock(TaskRelationManager.class)),
                new ListActivityTool(mock(WorkspaceActivityFilterManager.class), new McpActivityViewConverter()),

                new CreateTaskChecklistTool(mock(TaskChecklistManager.class), viewConverter),
                new RenameTaskChecklistTool(mock(TaskChecklistManager.class)),
                new DeleteTaskChecklistTool(mock(TaskChecklistManager.class)),
                new AddChecklistItemTool(mock(TaskChecklistItemManager.class), viewConverter),
                new UpdateChecklistItemTool(mock(TaskChecklistItemManager.class)),
                new DeleteChecklistItemTool(mock(TaskChecklistItemManager.class)),

                new ListTaskBoardsTool(mock(TaskBoardListingManager.class), viewConverter),
                new CreateTaskBoardTool(mock(TaskBoardManager.class), viewConverter),
                new AddTaskToBoardTool(mock(TaskBoardEntryManager.class)),
                new UpdateTaskBoardTool(mock(TaskBoardManager.class)),
                new RemoveTaskFromBoardTool(mock(TaskBoardEntryManager.class)),
                new MoveTaskOnBoardTool(mock(TaskBoardEntryManager.class)),
                new ListTopicsTool(mock(TopicListingManager.class), viewConverter),
                new CreateTopicTool(mock(TopicManager.class), viewConverter),

                new ListCalendarEventsTool(mock(CalendarEventManager.class), viewConverter),

                new ListNotebooksTool(mock(NotebookListingManager.class), viewConverter),
                new CreateNotebookTool(mock(NotebookOperationManager.class)),
                new SearchNotesTool(mock(NoteSearchManager.class), viewConverter),
                new ListNotesTool(mock(NoteSearchManager.class), viewConverter),
                new GetNoteTool(mock(NoteFilterManager.class), mock(NoteContentManager.class), viewConverter),
                new CreateNoteTool(mock(NoteContentManager.class), noteEditConverter, viewConverter),
                new ReplaceInNoteTool(mock(NoteContentManager.class), noteEditConverter, viewConverter),
                new AppendToNoteTool(mock(NoteContentManager.class), noteEditConverter, viewConverter),
                new UpdateNoteTitleTool(mock(NoteContentManager.class), noteEditConverter, viewConverter),
                new MoveNoteTool(mock(NoteOperationManager.class), mock(NoteFilterManager.class), noteEditConverter),
                new DeleteNoteTool(mock(NoteOperationManager.class)),

                new ListFilesTool(mock(MaterialListingManager.class), viewConverter),
                new SearchFilesTool(mock(MaterialListingManager.class), viewConverter),
                new GetFileLinkTool(mock(MaterialManager.class), linkConverter),

                new ListNotificationsTool(mock(NotificationEventListingManager.class), viewConverter),
                new GetUnreadNotificationCountTool(mock(NotificationEventListingManager.class)),

                new SearchTool(mock(WorkspaceManager.class), mock(TaskSearchManager.class),
                        mock(NoteSearchManager.class), mock(MaterialListingManager.class), feProperties, linkConverter),
                new FetchTool(mock(NoteFilterManager.class), mock(TaskRetrieveManager.class), mock(MaterialManager.class),
                        feProperties, new RichTextPlainTextConverter(), linkConverter));
    }

    static McpToolRegistry registry() {
        return new McpToolRegistry(tools());
    }

    private static <T> T mock(Class<T> type) {
        return Mockito.mock(type);
    }
}
