package co.jinear.core.service.mcp.tool.config;

import co.jinear.core.manager.project.ProjectManager;
import co.jinear.core.manager.project.ProjectMilestoneManager;
import co.jinear.core.manager.project.ProjectQueryManager;
import co.jinear.core.model.enumtype.mcp.McpScope;
import co.jinear.core.model.enumtype.project.ProjectPriorityType;
import co.jinear.core.model.enumtype.project.ProjectStateType;
import co.jinear.core.model.mcp.McpJsonSchema;
import co.jinear.core.model.mcp.McpToolException;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.request.project.*;
import co.jinear.core.service.mcp.tool.McpShapes;
import co.jinear.core.service.mcp.tool.McpTool;
import co.jinear.core.service.mcp.tool.McpToolArguments;
import co.jinear.core.service.mcp.tool.SimpleMcpTool;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Projects and their milestones. A project spans teams; a milestone is a checkpoint
 * inside one.
 */
@Configuration
@RequiredArgsConstructor
public class ProjectMcpTools {

    private static final List<String> STATES =
            List.of("BACKLOG", "PLANNED", "IN_PROGRESS", "COMPLETED", "CANCELLED");
    private static final List<String> PRIORITIES =
            List.of("NONE", "URGENT", "HIGH", "MEDIUM", "LOW");

    private final ProjectManager projectManager;
    private final ProjectQueryManager projectQueryManager;
    private final ProjectMilestoneManager projectMilestoneManager;

    @Bean
    public McpTool listProjectsTool() {
        return SimpleMcpTool.named("list_projects")
                .title("List projects")
                .description("Lists the projects in a workspace. By default only active projects are returned; "
                        + "set scope to ARCHIVED for archived ones, or ASSIGNED for the projects the signed in "
                        + "account leads or belongs to.")
                .input(McpJsonSchema.object()
                        .requiredString("workspaceId", "Workspace id, from list_workspaces.")
                        .enumeration("scope", "Which projects to return. Defaults to ALL.",
                                List.of("ALL", "ASSIGNED", "ARCHIVED"), false)
                        .integer("page", "Zero based page number. Defaults to 0.")
                        .build())
                .output(McpShapes.pageSchema("Projects in this workspace.", McpShapes.projectSchema()))
                .readOnly()
                .scopes(McpScope.PROJECTS_READ)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    String workspaceId = args.requiredString("workspaceId");
                    context.setWorkspaceId(workspaceId);
                    String scope = args.optionalString("scope", "ALL").toUpperCase(Locale.ROOT);
                    int page = args.page();
                    var result = switch (scope) {
                        case "ASSIGNED" -> projectQueryManager.retrieveAssigned(workspaceId, page).getProjects();
                        case "ARCHIVED" -> projectQueryManager.retrieveArchived(workspaceId, page).getProjects();
                        case "ALL" -> projectQueryManager.retrieveAll(workspaceId, page).getProjects();
                        default -> throw new McpToolException("invalid_argument",
                                "scope must be ALL, ASSIGNED or ARCHIVED. Received: " + scope);
                    };
                    return McpToolResult.of(McpShapes.page(result, McpShapes::project));
                })
                .build();
    }

    @Bean
    public McpTool getProjectTool() {
        return SimpleMcpTool.named("get_project")
                .title("Get one project")
                .description("Reads a single project by id, including its state, priority, dates and lead.")
                .input(McpJsonSchema.object()
                        .requiredString("projectId", "Project id, from list_projects.")
                        .build())
                .output(McpShapes.singleSchema("project", "The project.", McpShapes.projectSchema()))
                .readOnly()
                .scopes(McpScope.PROJECTS_READ)
                .handler((context, arguments) -> {
                    String projectId = McpToolArguments.of(arguments).requiredString("projectId");
                    var project = projectQueryManager.retrieve(projectId).getProjectDto();
                    context.setWorkspaceId(project.getWorkspaceId());
                    return McpToolResult.of(McpShapes.single("project", McpShapes.project(project)));
                })
                .build();
    }

    @Bean
    public McpTool createProjectTool() {
        return SimpleMcpTool.named("create_project")
                .title("Create a project")
                .description("Creates a project in a workspace and attaches it to one or more teams. "
                        + "A project needs at least one team, because its tasks live in team backlogs.")
                .input(McpJsonSchema.object()
                        .requiredString("workspaceId", "Workspace id, from list_workspaces.")
                        .requiredString("title", "Project name.")
                        .stringArray("teamIds", "Team ids this project spans, from list_teams. At least one is required.", true)
                        .string("description", "Project description. Plain text or simple HTML.")
                        .enumeration("state", "Starting state. Defaults to BACKLOG.", STATES, false)
                        .enumeration("priority", "Priority band. Defaults to NONE.", PRIORITIES, false)
                        .string("startDate", "ISO 8601 instant the project starts.")
                        .string("targetDate", "ISO 8601 instant the project is due.")
                        .build())
                .output(McpShapes.acknowledgementSchema("workspaceId", "The workspace the project was created in."))
                .write()
                .scopes(McpScope.PROJECTS_WRITE)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    List<String> teamIds = args.optionalStringList("teamIds");
                    if (teamIds.isEmpty()) {
                        return McpToolResult.error("teamIds must name at least one team. Call list_teams for the workspace.");
                    }
                    ProjectInitializeRequest request = new ProjectInitializeRequest();
                    request.setWorkspaceId(args.requiredString("workspaceId"));
                    request.setTitle(args.requiredString("title"));
                    request.setDescription(args.optionalString("description", null));
                    request.setTeamIds(teamIds);
                    request.setProjectState(parseState(args.optionalString("state", "BACKLOG")));
                    request.setProjectPriority(parsePriority(args.optionalString("priority", "NONE")));
                    request.setStartDate(args.optionalZonedDateTime("startDate"));
                    request.setTargetDate(args.optionalZonedDateTime("targetDate"));
                    context.setWorkspaceId(request.getWorkspaceId());
                    projectManager.initializeProject(request);
                    return McpToolResult.of(McpShapes.acknowledgement("workspaceId", request.getWorkspaceId()));
                })
                .build();
    }

    @Bean
    public McpTool updateProjectTool() {
        return SimpleMcpTool.named("update_project")
                .title("Update a project")
                .description("Changes a project's title, description, state, priority, dates or archived flag. "
                        + "Supply only the fields to change; anything omitted is left alone.")
                .input(McpJsonSchema.object()
                        .requiredString("projectId", "Project id, from list_projects.")
                        .string("title", "New project name.")
                        .string("description", "New description. Plain text or simple HTML.")
                        .enumeration("state", "New state.", STATES, false)
                        .enumeration("priority", "New priority band.", PRIORITIES, false)
                        .string("startDate", "New ISO 8601 start date.")
                        .string("targetDate", "New ISO 8601 target date.")
                        .bool("archived", "True to archive the project, false to restore it.")
                        .build())
                .output(McpShapes.acknowledgementSchema("projectId", "The project that was updated."))
                .write()
                .scopes(McpScope.PROJECTS_WRITE)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    String projectId = args.requiredString("projectId");
                    boolean changed = false;

                    if (args.has("title")) {
                        ProjectTitleUpdateRequest request = new ProjectTitleUpdateRequest();
                        request.setTitle(args.requiredString("title"));
                        projectManager.updateTitle(projectId, request);
                        changed = true;
                    }
                    if (args.has("description")) {
                        ProjectDescriptionUpdateRequest request = new ProjectDescriptionUpdateRequest();
                        request.setDescription(args.optionalString("description", ""));
                        projectManager.updateDescription(projectId, request);
                        changed = true;
                    }
                    if (args.has("state")) {
                        ProjectStateUpdateRequest request = new ProjectStateUpdateRequest();
                        request.setProjectState(parseState(args.requiredString("state")));
                        projectManager.updateState(projectId, request);
                        changed = true;
                    }
                    if (args.has("priority")) {
                        ProjectPriorityUpdateRequest request = new ProjectPriorityUpdateRequest();
                        request.setProjectPriority(parsePriority(args.requiredString("priority")));
                        projectManager.updatePriority(projectId, request);
                        changed = true;
                    }
                    if (args.has("startDate") || args.has("targetDate")) {
                        ProjectDatesUpdateRequest request = new ProjectDatesUpdateRequest();
                        request.setStartDate(args.optionalZonedDateTime("startDate"));
                        request.setTargetDate(args.optionalZonedDateTime("targetDate"));
                        projectManager.updateDates(projectId, request);
                        changed = true;
                    }
                    if (args.has("archived")) {
                        ProjectUpdateArchivedRequest request = new ProjectUpdateArchivedRequest();
                        request.setArchived(args.requiredBoolean("archived"));
                        projectManager.updateArchived(projectId, request);
                        changed = true;
                    }
                    if (!changed) {
                        return McpToolResult.error("Nothing to update. Supply at least one of title, description, "
                                + "state, priority, startDate, targetDate or archived.");
                    }
                    return McpToolResult.of(McpShapes.acknowledgement("projectId", projectId));
                })
                .build();
    }

    @Bean
    public McpTool listProjectMilestonesTool() {
        return SimpleMcpTool.named("list_project_milestones")
                .title("List a project's milestones")
                .description("Lists the milestones of one project, in project order. "
                        + "Milestone ids from here are what create_task and update_task take as milestoneId.")
                .input(McpJsonSchema.object()
                        .requiredString("projectId", "Project id, from list_projects.")
                        .build())
                .output(McpShapes.listSchema("Milestones in this project.", McpShapes.milestoneSchema()))
                .readOnly()
                .scopes(McpScope.PROJECTS_READ)
                .handler((context, arguments) -> {
                    String projectId = McpToolArguments.of(arguments).requiredString("projectId");
                    var project = projectQueryManager.retrieve(projectId).getProjectDto();
                    context.setWorkspaceId(project.getWorkspaceId());
                    var milestones = Objects.isNull(project.getMilestones()) ? List.of() : project.getMilestones();
                    var ordered = milestones.stream()
                            .map(co.jinear.core.model.dto.project.MilestoneDto.class::cast)
                            .sorted((first, second) -> {
                                int firstOrder = Objects.isNull(first.getMilestoneOrder()) ? Integer.MAX_VALUE : first.getMilestoneOrder();
                                int secondOrder = Objects.isNull(second.getMilestoneOrder()) ? Integer.MAX_VALUE : second.getMilestoneOrder();
                                return Integer.compare(firstOrder, secondOrder);
                            })
                            .toList();
                    return McpToolResult.of(McpShapes.list(ordered, McpShapes::milestone));
                })
                .build();
    }

    @Bean
    public McpTool createProjectMilestoneTool() {
        return SimpleMcpTool.named("create_project_milestone")
                .title("Create a milestone")
                .description("Adds a milestone to a project. Tasks can then be filed against it with create_task or update_task.")
                .input(McpJsonSchema.object()
                        .requiredString("projectId", "Project id, from list_projects.")
                        .requiredString("title", "Milestone name.")
                        .string("description", "Milestone description. Plain text or simple HTML.")
                        .string("targetDate", "ISO 8601 instant the milestone is due.")
                        .build())
                .output(McpShapes.acknowledgementSchema("projectId", "The project the milestone was added to."))
                .write()
                .scopes(McpScope.PROJECTS_WRITE)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    InitializeMilestoneRequest request = new InitializeMilestoneRequest();
                    request.setProjectId(args.requiredString("projectId"));
                    request.setTitle(args.requiredString("title"));
                    request.setDescription(args.optionalString("description", null));
                    request.setTargetDate(args.optionalZonedDateTime("targetDate"));
                    projectMilestoneManager.initialize(request);
                    return McpToolResult.of(McpShapes.acknowledgement("projectId", request.getProjectId()));
                })
                .build();
    }

    private ProjectStateType parseState(String value) {
        try {
            return ProjectStateType.valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new McpToolException("invalid_argument",
                    "state must be one of " + String.join(", ", STATES) + ". Received: " + value);
        }
    }

    private ProjectPriorityType parsePriority(String value) {
        try {
            return ProjectPriorityType.valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new McpToolException("invalid_argument",
                    "priority must be one of " + String.join(", ", PRIORITIES) + ". Received: " + value);
        }
    }

}
