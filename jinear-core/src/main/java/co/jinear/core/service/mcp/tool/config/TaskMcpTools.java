package co.jinear.core.service.mcp.tool.config;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.manager.task.*;
import co.jinear.core.model.dto.task.CommentDto;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import co.jinear.core.model.mcp.McpJsonSchema;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.request.task.*;
import co.jinear.core.service.mcp.tool.McpShapes;
import co.jinear.core.service.mcp.tool.McpTool;
import co.jinear.core.service.mcp.tool.McpToolArguments;
import co.jinear.core.service.mcp.tool.SimpleMcpTool;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class TaskMcpTools {

    private static final JsonNodeFactory FACTORY = JsonNodeFactory.instance;

    private final TaskInitializeManager taskInitializeManager;
    private final TaskListingManager taskListingManager;
    private final TaskRetrieveManager taskRetrieveManager;
    private final TaskSearchManager taskSearchManager;
    private final TaskUpdateManager taskUpdateManager;
    private final TaskCommentManager taskCommentManager;
    private final McpProperties mcpProperties;

    @Bean
    public McpTool searchTasksTool() {
        return SimpleMcpTool.named("search_tasks")
                .title("Search tasks by text")
                .description("Finds tasks in a workspace whose title or body matches a plain language query. "
                        + "Use this when the person names a task by what it is about rather than by its reference. "
                        + "Use list_tasks instead when filtering by status, assignee or dates.")
                .input(McpJsonSchema.object()
                        .requiredString("workspaceId", "Workspace id, from list_workspaces.")
                        .requiredString("query", "Free text to match against task titles and bodies.")
                        .stringArray("teamIds", "Restrict the search to these teams. Omit to search the whole workspace.", false)
                        .integer("page", "Zero based page number. Defaults to 0.")
                        .build())
                .output(McpShapes.pageSchema("Matching tasks, best match first.", McpShapes.taskSchema()))
                .readOnly()
                .scopes(OauthScope.TASKS_READ)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    TaskSearchRequest request = new TaskSearchRequest();
                    request.setWorkspaceId(args.requiredString("workspaceId"));
                    request.setQuery(args.requiredString("query"));
                    List<String> teamIds = args.optionalStringList("teamIds");
                    request.setTeamIdList(teamIds.isEmpty() ? null : teamIds);
                    context.setWorkspaceId(request.getWorkspaceId());
                    var page = taskSearchManager.searchTask(request, args.page()).getResult();
                    return McpToolResult.of(McpShapes.page(page, McpShapes::task));
                })
                .build();
    }

    @Bean
    public McpTool listTasksTool() {
        return SimpleMcpTool.named("list_tasks")
                .title("List tasks with filters")
                .description("Lists tasks in a workspace, optionally narrowed by team, assignee, workflow status, "
                        + "state group or a date range. "
                        + "This is the tool for questions like what is in progress, what is assigned to someone, "
                        + "or what is due this week.")
                .input(McpJsonSchema.object()
                        .requiredString("workspaceId", "Workspace id, from list_workspaces.")
                        .stringArray("teamIds", "Restrict to these teams.", false)
                        .stringArray("assigneeIds", "Restrict to tasks assigned to these account ids, from list_workspace_members.", false)
                        .stringArray("workflowStatusIds", "Restrict to these status ids, from list_workflow_statuses.", false)
                        .stringArray("stateGroups", "Restrict by state group. Any of BACKLOG, NOT_STARTED, STARTED, COMPLETED, CANCELLED.", false)
                        .string("from", "Only tasks whose dates fall on or after this ISO 8601 instant.")
                        .string("to", "Only tasks whose dates fall on or before this ISO 8601 instant.")
                        .withPaging(50)
                        .build())
                .output(McpShapes.pageSchema("Matching tasks, newest first.", McpShapes.taskSchema()))
                .readOnly()
                .scopes(OauthScope.TASKS_READ)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    TaskFilterRequest request = new TaskFilterRequest();
                    request.setWorkspaceId(args.requiredString("workspaceId"));
                    request.setPage(args.page());
                    request.setSize(args.pageSize(mcpProperties.getMaxPageSize()));
                    request.setTeamIdList(nullIfEmpty(args.optionalStringList("teamIds")));
                    request.setAssigneeIds(nullIfEmpty(args.optionalStringList("assigneeIds")));
                    request.setWorkflowStatusIdList(nullIfEmpty(args.optionalStringList("workflowStatusIds")));
                    request.setWorkflowStateGroups(parseStateGroups(args.optionalStringList("stateGroups")));
                    request.setTimespanStart(args.optionalZonedDateTime("from"));
                    request.setTimespanEnd(args.optionalZonedDateTime("to"));
                    context.setWorkspaceId(request.getWorkspaceId());
                    var page = taskListingManager.filterTasks(request).getTaskDtoPage();
                    return McpToolResult.of(McpShapes.page(page, McpShapes::task));
                })
                .build();
    }

    @Bean
    public McpTool getTaskTool() {
        return SimpleMcpTool.named("get_task")
                .title("Get one task")
                .description("Reads a single task in full, including its body, by its human readable reference such as ENG-42. "
                        + "Use it after search_tasks or list_tasks when the body of a specific task is needed.")
                .input(McpJsonSchema.object()
                        .requiredString("workspaceUsername", "Workspace handle, the username field from list_workspaces.")
                        .requiredString("teamTag", "Team tag, the part before the dash in a reference such as ENG-42.")
                        .requiredInteger("taskNumber", "Task number, the part after the dash in a reference such as ENG-42.")
                        .build())
                .output(McpShapes.singleSchema("task", "The task, with its body.", McpShapes.taskSchema()))
                .readOnly()
                .scopes(OauthScope.TASKS_READ)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    var response = taskRetrieveManager.retrieveWithWorkspaceNameAndTeamTagNo(
                            args.requiredString("workspaceUsername"),
                            args.requiredString("teamTag"),
                            requiredTaskNumber(args));
                    context.setWorkspaceId(response.getTaskDto().getWorkspaceId());
                    return McpToolResult.of(McpShapes.single("task", McpShapes.taskDetail(response.getTaskDto())));
                })
                .build();
    }

    @Bean
    public McpTool createTaskTool() {
        return SimpleMcpTool.named("create_task")
                .title("Create a task")
                .description("Creates a task in a team. Only workspaceId, teamId and title are required. "
                        + "Give it a due date to put it on the calendar, and an assignee account id to hand it to someone. "
                        + "Returns the created task, including its reference such as ENG-42.")
                .input(McpJsonSchema.object()
                        .requiredString("workspaceId", "Workspace id, from list_workspaces.")
                        .requiredString("teamId", "Team id, from list_teams.")
                        .requiredString("title", "One line summary of the work.")
                        .string("description", "Task body. Plain text or simple HTML.")
                        .string("assignedTo", "Account id of the assignee, from list_workspace_members.")
                        .string("startDate", "ISO 8601 instant the work should start.")
                        .string("dueDate", "ISO 8601 instant the work is due.")
                        .string("topicId", "Label to apply, from list_topics on the team.")
                        .string("boardId", "Board to add the task to on creation.")
                        .build())
                .output(McpShapes.singleSchema("task", "The created task.", McpShapes.taskSchema()))
                .write()
                .scopes(OauthScope.TASKS_WRITE)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    TaskInitializeRequest request = new TaskInitializeRequest();
                    request.setWorkspaceId(args.requiredString("workspaceId"));
                    request.setTeamId(args.requiredString("teamId"));
                    request.setTitle(args.requiredString("title"));
                    request.setDescription(args.optionalString("description", null));
                    request.setAssignedTo(args.optionalString("assignedTo", null));
                    request.setAssignedDate(args.optionalZonedDateTime("startDate"));
                    request.setDueDate(args.optionalZonedDateTime("dueDate"));
                    request.setHasPreciseAssignedDate(args.has("startDate"));
                    request.setHasPreciseDueDate(args.has("dueDate"));
                    request.setTopicId(args.optionalString("topicId", null));
                    request.setBoardId(args.optionalString("boardId", null));
                    context.setWorkspaceId(request.getWorkspaceId());
                    var response = taskInitializeManager.initializeTask(request);
                    return McpToolResult.of(McpShapes.single("task", McpShapes.task(response.getTaskDto())));
                })
                .build();
    }

    @Bean
    public McpTool updateTaskTool() {
        return SimpleMcpTool.named("update_task")
                .title("Update a task")
                .description("Changes a task's title, body, dates or assignee. "
                        + "Supply only the fields to change; anything omitted is left alone. "
                        + "Use set_task_status to move a task between workflow statuses.")
                .input(McpJsonSchema.object()
                        .requiredString("taskId", "Task id, from search_tasks, list_tasks or get_task.")
                        .string("title", "New one line summary.")
                        .string("description", "New task body. Plain text or simple HTML.")
                        .string("assignedTo", "Account id of the new assignee. Pass an empty string to unassign.")
                        .string("startDate", "New ISO 8601 start. Pass an empty string to clear it.")
                        .string("dueDate", "New ISO 8601 due date. Pass an empty string to clear it.")
                        .build())
                .output(McpShapes.acknowledgementSchema("taskId", "The task that was updated."))
                .write()
                .scopes(OauthScope.TASKS_WRITE)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    String taskId = args.requiredString("taskId");
                    boolean changed = false;

                    if (args.has("title")) {
                        TaskUpdateTitleRequest request = new TaskUpdateTitleRequest();
                        request.setTitle(args.requiredString("title"));
                        taskUpdateManager.updateTaskTitle(taskId, request);
                        changed = true;
                    }
                    if (args.has("description")) {
                        TaskUpdateDescriptionRequest request = new TaskUpdateDescriptionRequest();
                        request.setDescription(args.optionalString("description", ""));
                        taskUpdateManager.updateTaskDescription(taskId, request);
                        changed = true;
                    }
                    if (args.has("assignedTo")) {
                        TaskAssigneeUpdateRequest request = new TaskAssigneeUpdateRequest();
                        String assignee = args.optionalString("assignedTo", "");
                        request.setAssigneeId(assignee.isBlank() ? null : assignee);
                        taskUpdateManager.updateTaskAssignee(taskId, request);
                        changed = true;
                    }
                    if (args.has("startDate") || args.has("dueDate")) {
                        TaskDateUpdateRequest request = new TaskDateUpdateRequest();
                        request.setAssignedDate(args.optionalZonedDateTime("startDate"));
                        request.setDueDate(args.optionalZonedDateTime("dueDate"));
                        request.setHasPreciseAssignedDate(Objects.nonNull(request.getAssignedDate()));
                        request.setHasPreciseDueDate(Objects.nonNull(request.getDueDate()));
                        taskUpdateManager.updateTaskDates(taskId, request);
                        changed = true;
                    }
                    if (!changed) {
                        return McpToolResult.error("Nothing to update. Supply at least one of title, description, "
                                + "assignedTo, startDate or dueDate.");
                    }
                    return McpToolResult.of(McpShapes.acknowledgement("taskId", taskId));
                })
                .build();
    }

    @Bean
    public McpTool setTaskStatusTool() {
        return SimpleMcpTool.named("set_task_status")
                .title("Move a task to a workflow status")
                .description("Moves a task into one of its team's workflow statuses, for example from In Progress to Done. "
                        + "Statuses are per team and referenced by id, so call list_workflow_statuses for the task's team first. "
                        + "Jinear does not delete tasks; retiring one means moving it to a status in the CANCELLED group.")
                .input(McpJsonSchema.object()
                        .requiredString("taskId", "Task id.")
                        .requiredString("workflowStatusId", "Status id, from list_workflow_statuses for the task's team.")
                        .build())
                .output(McpShapes.singleSchema("task", "The task in its new status.", McpShapes.taskSchema()))
                .write()
                .idempotent()
                .scopes(OauthScope.TASKS_WRITE)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    var response = taskUpdateManager.updateTaskWorkflowStatus(
                            args.requiredString("taskId"),
                            args.requiredString("workflowStatusId"));
                    context.setWorkspaceId(response.getTaskDto().getWorkspaceId());
                    return McpToolResult.of(McpShapes.single("task", McpShapes.task(response.getTaskDto())));
                })
                .build();
    }

    @Bean
    public McpTool listTaskCommentsTool() {
        return SimpleMcpTool.named("list_task_comments")
                .title("List a task's comments")
                .description("Reads the discussion on a task, newest first. "
                        + "Use it to catch up on what has already been said before answering or adding a comment.")
                .input(McpJsonSchema.object()
                        .requiredString("taskId", "Task id.")
                        .integer("page", "Zero based page number. Defaults to 0.")
                        .build())
                .output(McpShapes.pageSchema("Comments on this task.", commentSchema()))
                .readOnly()
                .scopes(OauthScope.TASKS_READ)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    var page = taskCommentManager.retrieveTaskComments(args.requiredString("taskId"), args.page())
                            .getCommentsPage();
                    return McpToolResult.of(McpShapes.page(page, TaskMcpTools::comment));
                })
                .build();
    }

    @Bean
    public McpTool addTaskCommentTool() {
        return SimpleMcpTool.named("add_task_comment")
                .title("Comment on a task")
                .description("Adds a comment to a task, attributed to the signed in account. "
                        + "Use it to record a decision, an update or an answer where the work lives.")
                .input(McpJsonSchema.object()
                        .requiredString("taskId", "Task id.")
                        .requiredString("comment", "Comment body. Plain text or simple HTML.")
                        .string("quoteCommentId", "Comment id being replied to, if this is a reply.")
                        .build())
                .output(McpShapes.acknowledgementSchema("taskId", "The task the comment was added to."))
                .write()
                .scopes(OauthScope.TASKS_WRITE)
                .handler((context, arguments) -> {
                    McpToolArguments args = McpToolArguments.of(arguments);
                    InitializeTaskCommentRequest request = new InitializeTaskCommentRequest();
                    request.setTaskId(args.requiredString("taskId"));
                    request.setComment(args.requiredString("comment"));
                    request.setQuoteCommentId(args.optionalString("quoteCommentId", null));
                    taskCommentManager.initializeComment(request);
                    return McpToolResult.of(McpShapes.acknowledgement("taskId", request.getTaskId()));
                })
                .build();
    }

    private static ObjectNode comment(CommentDto dto) {
        ObjectNode node = FACTORY.objectNode();
        node.put("commentId", dto.getCommentId());
        node.put("taskId", dto.getTaskId());
        node.put("authorAccountId", dto.getOwnerId());
        node.put("authorUsername", Objects.isNull(dto.getOwner()) ? null : dto.getOwner().getUsername());
        node.put("body", Objects.isNull(dto.getRichText()) ? null : dto.getRichText().getValue());
        node.put("createdAt", Objects.isNull(dto.getCreatedDate())
                ? null
                : DateTimeFormatter.ISO_INSTANT.format(dto.getCreatedDate().toInstant()));
        return node;
    }

    private static ObjectNode commentSchema() {
        return McpJsonSchema.object()
                .string("commentId", "Comment id.")
                .string("taskId", "Task this comment belongs to.")
                .string("authorAccountId", "Account id of the author.")
                .string("authorUsername", "Author handle.")
                .string("body", "Comment body as HTML.")
                .string("createdAt", "ISO 8601 instant the comment was posted.")
                .build();
    }

    private int requiredTaskNumber(McpToolArguments args) {
        Integer number = args.optionalInteger("taskNumber", null);
        if (Objects.isNull(number)) {
            throw new co.jinear.core.model.mcp.McpToolException("missing_argument",
                    "taskNumber is required. In the reference ENG-42 it is 42.");
        }
        return number;
    }

    private List<String> nullIfEmpty(List<String> values) {
        return values.isEmpty() ? null : values;
    }

    private List<TeamWorkflowStateGroup> parseStateGroups(List<String> values) {
        if (values.isEmpty()) {
            return null;
        }
        return values.stream()
                .map(value -> {
                    try {
                        return TeamWorkflowStateGroup.valueOf(value.toUpperCase(java.util.Locale.ROOT));
                    } catch (IllegalArgumentException exception) {
                        throw new co.jinear.core.model.mcp.McpToolException("invalid_argument",
                                "stateGroups must contain only BACKLOG, NOT_STARTED, STARTED, COMPLETED or CANCELLED. Received: " + value);
                    }
                })
                .toList();
    }
}
