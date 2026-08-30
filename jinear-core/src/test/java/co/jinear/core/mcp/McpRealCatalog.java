package co.jinear.core.mcp;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.manager.calendar.CalendarEventManager;
import co.jinear.core.manager.material.MaterialListingManager;
import co.jinear.core.manager.note.NoteFilterManager;
import co.jinear.core.manager.notebook.NotebookListingManager;
import co.jinear.core.manager.project.ProjectManager;
import co.jinear.core.manager.project.ProjectMilestoneManager;
import co.jinear.core.manager.project.ProjectQueryManager;
import co.jinear.core.manager.task.*;
import co.jinear.core.manager.team.TeamRetrieveManager;
import co.jinear.core.manager.team.TeamWorkflowStatusManager;
import co.jinear.core.manager.topic.TopicListingManager;
import co.jinear.core.manager.workspace.WorkspaceManager;
import co.jinear.core.manager.workspace.WorkspaceMemberRetrieveManager;
import co.jinear.core.service.mcp.tool.McpTool;
import co.jinear.core.service.mcp.tool.McpToolRegistry;
import co.jinear.core.service.mcp.tool.config.*;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds the real tool catalog with every manager mocked.
 * <p>
 * The definitions are what the directory reviews and what the published documentation is
 * generated from, and none of them depend on a live manager, so they can be inspected
 * without a Spring context or a database.
 */
final class McpRealCatalog {

    private McpRealCatalog() {
    }

    static McpProperties properties() {
        McpProperties properties = new McpProperties();
        properties.setEnabled(Boolean.TRUE);
        properties.setIssuerUrl("https://api.jinear.co");
        properties.setResourceUrl("https://api.jinear.co/mcp");
        properties.setDocumentationUrl("https://jinear.co/mcp/");
        properties.setMaxPageSize(50);
        return properties;
    }

    static List<McpTool> tools() {
        McpProperties properties = properties();
        FeProperties feProperties = new FeProperties();

        WorkspaceMcpTools workspaceTools = new WorkspaceMcpTools(
                Mockito.mock(WorkspaceManager.class),
                Mockito.mock(WorkspaceMemberRetrieveManager.class),
                Mockito.mock(TeamRetrieveManager.class),
                Mockito.mock(TeamWorkflowStatusManager.class),
                properties);

        TaskMcpTools taskTools = new TaskMcpTools(
                Mockito.mock(TaskInitializeManager.class),
                Mockito.mock(TaskListingManager.class),
                Mockito.mock(TaskRetrieveManager.class),
                Mockito.mock(TaskSearchManager.class),
                Mockito.mock(TaskUpdateManager.class),
                Mockito.mock(TaskCommentManager.class),
                properties);

        BoardMcpTools boardTools = new BoardMcpTools(
                Mockito.mock(TaskBoardManager.class),
                Mockito.mock(TaskBoardListingManager.class),
                Mockito.mock(TaskBoardEntryManager.class),
                Mockito.mock(TopicListingManager.class));

        ProjectMcpTools projectTools = new ProjectMcpTools(
                Mockito.mock(ProjectManager.class),
                Mockito.mock(ProjectQueryManager.class),
                Mockito.mock(ProjectMilestoneManager.class));

        CalendarMcpTools calendarTools = new CalendarMcpTools(Mockito.mock(CalendarEventManager.class));

        NoteMcpTools noteTools = new NoteMcpTools(
                Mockito.mock(NotebookListingManager.class),
                Mockito.mock(NoteFilterManager.class));

        FileMcpTools fileTools = new FileMcpTools(Mockito.mock(MaterialListingManager.class), properties);

        CompatibilityMcpTools compatibilityTools = new CompatibilityMcpTools(
                Mockito.mock(WorkspaceManager.class),
                Mockito.mock(TaskSearchManager.class),
                Mockito.mock(NoteFilterManager.class),
                feProperties);

        List<McpTool> tools = new ArrayList<>(List.of(
                workspaceTools.listWorkspacesTool(),
                workspaceTools.getWorkspaceTool(),
                workspaceTools.listTeamsTool(),
                workspaceTools.listWorkflowStatusesTool(),
                workspaceTools.listWorkspaceMembersTool(),

                taskTools.searchTasksTool(),
                taskTools.listTasksTool(),
                taskTools.getTaskTool(),
                taskTools.createTaskTool(),
                taskTools.updateTaskTool(),
                taskTools.setTaskStatusTool(),
                taskTools.listTaskCommentsTool(),
                taskTools.addTaskCommentTool()));

        tools.addAll(List.of(
                boardTools.listTaskBoardsTool(),
                boardTools.createTaskBoardTool(),
                boardTools.addTaskToBoardTool(),
                boardTools.listTopicsTool(),

                projectTools.listProjectsTool(),
                projectTools.getProjectTool(),
                projectTools.createProjectTool(),
                projectTools.updateProjectTool(),
                projectTools.listProjectMilestonesTool(),
                projectTools.createProjectMilestoneTool(),

                calendarTools.listCalendarEventsTool(),

                noteTools.listNotebooksTool(),
                noteTools.searchNotesTool(),
                noteTools.getNoteTool(),

                fileTools.listFilesTool(),
                fileTools.getFileLinkTool(),

                compatibilityTools.searchTool(),
                compatibilityTools.fetchTool()));

        return tools;
    }

    static McpToolRegistry registry() {
        McpToolRegistry registry = new McpToolRegistry(tools());
        ReflectionTestUtils.invokeMethod(registry, "index");
        return registry;
    }
}
