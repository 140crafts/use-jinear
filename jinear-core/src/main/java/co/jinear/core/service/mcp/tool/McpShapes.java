package co.jinear.core.service.mcp.tool;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.calendar.CalendarEventDto;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.notebook.NotebookDto;
import co.jinear.core.model.dto.project.MilestoneDto;
import co.jinear.core.model.dto.project.ProjectDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.task.TaskBoardDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.dto.workspace.DetailedWorkspaceMemberDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.dto.workspace.WorkspaceMemberDto;
import co.jinear.core.model.mcp.McpJsonSchema;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.experimental.UtilityClass;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * How a Jinear record looks on the wire, and the schema that describes it.
 * <p>
 * Each record is projected down to the fields an agent can act on. Returning the full
 * DTO would drag in rich text state, nested workspaces and media descriptors, which
 * costs the model context it cannot use and is one of the things a directory review
 * flags as an oversized response.
 * <p>
 * Mapper and schema live next to each other on purpose: they are the same contract seen
 * twice, and the published documentation is generated from the schema half.
 */
@UtilityClass
public class McpShapes {

    private static final JsonNodeFactory FACTORY = JsonNodeFactory.instance;

    // --- workspace -------------------------------------------------------------

    public static ObjectNode workspace(WorkspaceDto dto) {
        ObjectNode node = FACTORY.objectNode();
        node.put("workspaceId", dto.getWorkspaceId());
        node.put("username", dto.getUsername());
        node.put("title", dto.getTitle());
        putEnum(node, "tier", dto.getTier());
        return node;
    }

    public static ObjectNode workspaceMembership(DetailedWorkspaceMemberDto dto) {
        ObjectNode node = Objects.isNull(dto.getWorkspace())
                ? FACTORY.objectNode()
                : workspace(dto.getWorkspace());
        putEnum(node, "role", dto.getRole());
        return node;
    }

    public static ObjectNode workspaceSchema() {
        return McpJsonSchema.object()
                .string("workspaceId", "Workspace id. Pass this to any tool that takes workspaceId.")
                .string("username", "Short workspace handle used in Jinear URLs.")
                .string("title", "Display name.")
                .string("tier", "Billing tier, BASIC or PRO.")
                .string("role", "The caller's role in this workspace.")
                .build();
    }

    public static ObjectNode member(WorkspaceMemberDto dto) {
        ObjectNode node = FACTORY.objectNode();
        node.put("workspaceMemberId", dto.getWorkspaceMemberId());
        node.put("accountId", dto.getAccountId());
        putEnum(node, "role", dto.getRole());
        if (Objects.nonNull(dto.getAccount())) {
            node.put("username", dto.getAccount().getUsername());
            node.put("email", dto.getAccount().getEmail());
        }
        return node;
    }

    public static ObjectNode memberSchema() {
        return McpJsonSchema.object()
                .string("workspaceMemberId", "Membership id.")
                .string("accountId", "Account id. This is the value assignee fields take.")
                .string("role", "OWNER, ADMIN or MEMBER.")
                .string("username", "Account handle.")
                .string("email", "Account email.")
                .build();
    }

    // --- team ------------------------------------------------------------------

    public static ObjectNode team(TeamDto dto) {
        ObjectNode node = FACTORY.objectNode();
        node.put("teamId", dto.getTeamId());
        node.put("workspaceId", dto.getWorkspaceId());
        node.put("name", dto.getName());
        node.put("username", dto.getUsername());
        node.put("tag", dto.getTag());
        putEnum(node, "taskVisibility", dto.getTaskVisibility());
        putEnum(node, "teamState", dto.getTeamState());
        return node;
    }

    public static ObjectNode teamSchema() {
        return McpJsonSchema.object()
                .string("teamId", "Team id. Required by every task tool.")
                .string("workspaceId", "Workspace this team belongs to.")
                .string("name", "Display name.")
                .string("username", "Short team handle used in Jinear URLs.")
                .string("tag", "Short prefix used in task references, as in ENG-42.")
                .string("taskVisibility", "Who can see this team's tasks.")
                .string("teamState", "ACTIVE or ARCHIVED.")
                .build();
    }

    public static ObjectNode workflowStatus(TeamWorkflowStatusDto dto) {
        ObjectNode node = FACTORY.objectNode();
        node.put("workflowStatusId", dto.getTeamWorkflowStatusId());
        node.put("teamId", dto.getTeamId());
        node.put("name", dto.getName());
        putEnum(node, "stateGroup", dto.getWorkflowStateGroup());
        node.put("order", dto.getOrder());
        return node;
    }

    public static ObjectNode workflowStatusSchema() {
        return McpJsonSchema.object()
                .string("workflowStatusId", "Status id. This is what set_task_status takes.")
                .string("teamId", "Team this status belongs to.")
                .string("name", "Display name, for example In Progress.")
                .string("stateGroup", "BACKLOG, NOT_STARTED, STARTED, COMPLETED or CANCELLED.")
                .integer("order", "Position in the team's board, left to right.")
                .build();
    }

    public static ObjectNode topic(TopicDto dto) {
        ObjectNode node = FACTORY.objectNode();
        node.put("topicId", dto.getTopicId());
        node.put("teamId", dto.getTeamId());
        node.put("name", dto.getName());
        node.put("tag", dto.getTag());
        node.put("color", dto.getColor());
        return node;
    }

    // --- task ------------------------------------------------------------------

    public static ObjectNode task(TaskDto dto) {
        ObjectNode node = FACTORY.objectNode();
        node.put("taskId", dto.getTaskId());
        node.put("workspaceId", dto.getWorkspaceId());
        node.put("teamId", dto.getTeamId());
        node.put("title", dto.getTitle());
        node.put("reference", reference(dto));
        node.put("workflowStatusId", dto.getWorkflowStatusId());
        if (Objects.nonNull(dto.getWorkflowStatus())) {
            node.put("workflowStatusName", dto.getWorkflowStatus().getName());
            putEnum(node, "workflowStateGroup", dto.getWorkflowStatus().getWorkflowStateGroup());
        }
        node.put("assignedTo", dto.getAssignedTo());
        node.put("ownerId", dto.getOwnerId());
        putDate(node, "assignedDate", dto.getAssignedDate());
        putDate(node, "dueDate", dto.getDueDate());
        node.put("projectId", dto.getProjectId());
        node.put("milestoneId", dto.getMilestoneId());
        node.put("topicId", dto.getTopicId());
        return node;
    }

    /** The detail view adds the body, which is too large to include in a list. */
    public static ObjectNode taskDetail(TaskDto dto) {
        ObjectNode node = task(dto);
        node.put("description", richText(dto.getDescription()));
        return node;
    }

    public static ObjectNode taskSchema() {
        return McpJsonSchema.object()
                .string("taskId", "Task id.")
                .string("workspaceId", "Workspace this task belongs to.")
                .string("teamId", "Team this task belongs to.")
                .string("title", "One line summary.")
                .string("reference", "Human readable reference, for example ENG-42.")
                .string("workflowStatusId", "Current status id.")
                .string("workflowStatusName", "Current status name.")
                .string("workflowStateGroup", "BACKLOG, NOT_STARTED, STARTED, COMPLETED or CANCELLED.")
                .string("assignedTo", "Account id of the assignee, or null.")
                .string("ownerId", "Account id of whoever created the task.")
                .string("assignedDate", "ISO 8601 start date, or null.")
                .string("dueDate", "ISO 8601 due date, or null.")
                .string("projectId", "Project this task belongs to, or null.")
                .string("milestoneId", "Milestone this task belongs to, or null.")
                .string("topicId", "Label applied to this task, or null.")
                .build();
    }

    public static ObjectNode taskDetailSchema() {
        return McpJsonSchema.object()
                .nested("task", "The task.", taskSchema(), true)
                .string("description", "Task body as HTML, or null when the task has no body.")
                .build();
    }

    public static ObjectNode board(TaskBoardDto dto) {
        ObjectNode node = FACTORY.objectNode();
        node.put("taskBoardId", dto.getTaskBoardId());
        node.put("workspaceId", dto.getWorkspaceId());
        node.put("teamId", dto.getTeamId());
        node.put("title", dto.getTitle());
        putEnum(node, "state", dto.getState());
        putDate(node, "dueDate", dto.getDueDate());
        return node;
    }

    public static ObjectNode boardSchema() {
        return McpJsonSchema.object()
                .string("taskBoardId", "Board id.")
                .string("workspaceId", "Workspace this board belongs to.")
                .string("teamId", "Team this board belongs to.")
                .string("title", "Board name.")
                .string("state", "Board lifecycle state.")
                .string("dueDate", "ISO 8601 due date, or null.")
                .build();
    }

    // --- project ---------------------------------------------------------------

    public static ObjectNode project(ProjectDto dto) {
        ObjectNode node = FACTORY.objectNode();
        node.put("projectId", dto.getProjectId());
        node.put("workspaceId", dto.getWorkspaceId());
        node.put("title", dto.getTitle());
        putEnum(node, "state", dto.getProjectState());
        putEnum(node, "priority", dto.getProjectPriority());
        putDate(node, "startDate", dto.getStartDate());
        putDate(node, "targetDate", dto.getTargetDate());
        node.put("archived", Boolean.TRUE.equals(dto.getArchived()));
        node.put("leadWorkspaceMemberId", dto.getLeadWorkspaceMemberId());
        return node;
    }

    public static ObjectNode projectSchema() {
        return McpJsonSchema.object()
                .string("projectId", "Project id.")
                .string("workspaceId", "Workspace this project belongs to.")
                .string("title", "Project name.")
                .string("state", "Lifecycle state, for example IN_PROGRESS.")
                .string("priority", "Priority band, for example HIGH.")
                .string("startDate", "ISO 8601 start date, or null.")
                .string("targetDate", "ISO 8601 target date, or null.")
                .bool("archived", "True when the project is archived.")
                .string("leadWorkspaceMemberId", "Membership id of the project lead, or null.")
                .build();
    }

    public static ObjectNode milestone(MilestoneDto dto) {
        ObjectNode node = FACTORY.objectNode();
        node.put("milestoneId", dto.getMilestoneId());
        node.put("projectId", dto.getProjectId());
        node.put("title", dto.getTitle());
        putEnum(node, "state", dto.getMilestoneState());
        putDate(node, "targetDate", dto.getTargetDate());
        node.put("order", dto.getMilestoneOrder());
        return node;
    }

    public static ObjectNode milestoneSchema() {
        return McpJsonSchema.object()
                .string("milestoneId", "Milestone id.")
                .string("projectId", "Project this milestone belongs to.")
                .string("title", "Milestone name.")
                .string("state", "Milestone state.")
                .string("targetDate", "ISO 8601 target date, or null.")
                .integer("order", "Position within the project.")
                .build();
    }

    // --- notes -----------------------------------------------------------------

    public static ObjectNode note(NoteDto dto) {
        ObjectNode node = FACTORY.objectNode();
        node.put("noteId", dto.getNoteId());
        node.put("notebookId", dto.getNotebookId());
        node.put("workspaceId", dto.getWorkspaceId());
        node.put("title", dto.getTitle());
        node.put("parentNoteId", dto.getParentNoteId());
        node.put("ownerId", dto.getOwnerId());
        return node;
    }

    public static ObjectNode noteDetail(NoteDto dto) {
        ObjectNode node = note(dto);
        node.put("body", richText(dto.getRichText()));
        return node;
    }

    public static ObjectNode noteSchema() {
        return McpJsonSchema.object()
                .string("noteId", "Note id.")
                .string("notebookId", "Notebook this note lives in.")
                .string("workspaceId", "Workspace this note belongs to.")
                .string("title", "Note title.")
                .string("parentNoteId", "Parent note when the note is nested, otherwise null.")
                .string("ownerId", "Account id of the author.")
                .build();
    }

    public static ObjectNode notebook(NotebookDto dto) {
        ObjectNode node = FACTORY.objectNode();
        node.put("notebookId", dto.getNotebookId());
        node.put("workspaceId", dto.getWorkspaceId());
        node.put("title", dto.getTitle());
        node.put("description", dto.getDescription());
        putEnum(node, "visibility", dto.getVisibility());
        return node;
    }

    public static ObjectNode notebookSchema() {
        return McpJsonSchema.object()
                .string("notebookId", "Notebook id.")
                .string("workspaceId", "Workspace this notebook belongs to.")
                .string("title", "Notebook name.")
                .string("description", "Notebook description, or null.")
                .string("visibility", "Who can see this notebook.")
                .build();
    }

    // --- calendar --------------------------------------------------------------

    public static ObjectNode calendarEvent(CalendarEventDto dto) {
        ObjectNode node = FACTORY.objectNode();
        node.put("calendarEventId", dto.getCalendarEventId());
        node.put("calendarId", dto.getCalendarId());
        node.put("workspaceId", dto.getWorkspaceId());
        node.put("title", dto.getTitle());
        putDate(node, "startsAt", dto.getAssignedDate());
        putDate(node, "endsAt", dto.getDueDate());
        node.put("location", dto.getLocation());
        putEnum(node, "sourceType", dto.getCalendarEventSourceType());
        node.put("relatedTaskId", Objects.isNull(dto.getRelatedTask()) ? null : dto.getRelatedTask().getTaskId());
        return node;
    }

    public static ObjectNode calendarEventSchema() {
        return McpJsonSchema.object()
                .string("calendarEventId", "Event id.")
                .string("calendarId", "Calendar this event belongs to.")
                .string("workspaceId", "Workspace this event belongs to.")
                .string("title", "Event title.")
                .string("startsAt", "ISO 8601 start.")
                .string("endsAt", "ISO 8601 end, or null for an all day event.")
                .string("location", "Free text location, or null.")
                .string("sourceType", "JINEAR for a native event, GOOGLE_CALENDAR for a synced one, TASK for a task shown on the calendar.")
                .string("relatedTaskId", "Task this event mirrors, or null.")
                .build();
    }

    // --- files -----------------------------------------------------------------

    public static ObjectNode file(MaterialDto dto) {
        ObjectNode node = FACTORY.objectNode();
        node.put("materialId", dto.getMaterialId());
        node.put("workspaceId", dto.getWorkspaceId());
        node.put("name", dto.getName());
        putEnum(node, "materialType", dto.getMaterialType());
        node.put("parentMaterialId", dto.getParentMaterialId());
        node.put("mediaId", dto.getMediaId());
        putEnum(node, "accessType", dto.getMaterialAccessType());
        return node;
    }

    public static ObjectNode fileSchema() {
        return McpJsonSchema.object()
                .string("materialId", "File or folder id.")
                .string("workspaceId", "Workspace this item belongs to.")
                .string("name", "File or folder name.")
                .string("materialType", "FOLDER or FILE.")
                .string("parentMaterialId", "Containing folder, or null at the root.")
                .string("mediaId", "Stored media id for a file, null for a folder.")
                .string("accessType", "Who can reach this item.")
                .build();
    }

    // --- envelopes -------------------------------------------------------------

    public static <T> ObjectNode page(PageDto<T> pageDto, Function<T, ObjectNode> mapper) {
        ObjectNode node = FACTORY.objectNode();
        ArrayNode items = node.putArray("items");
        if (Objects.nonNull(pageDto.getContent())) {
            pageDto.getContent().forEach(item -> items.add(mapper.apply(item)));
        }
        node.put("page", pageDto.getNumber());
        node.put("pageSize", pageDto.getSize());
        node.put("totalElements", pageDto.getTotalElements());
        node.put("totalPages", pageDto.getTotalPages());
        node.put("hasNext", pageDto.isHasNext());
        return node;
    }

    public static <T> ObjectNode list(Collection<T> items, Function<T, ObjectNode> mapper) {
        ObjectNode node = FACTORY.objectNode();
        ArrayNode array = node.putArray("items");
        if (Objects.nonNull(items)) {
            items.forEach(item -> array.add(mapper.apply(item)));
        }
        node.put("count", Objects.isNull(items) ? 0 : items.size());
        return node;
    }

    public static ObjectNode pageSchema(String itemDescription, ObjectNode itemSchema) {
        return McpJsonSchema.object()
                .objectArray("items", itemDescription, itemSchema, true)
                .integer("page", "Zero based page number of this result.")
                .integer("pageSize", "Items per page.")
                .integer("totalElements", "Total matching items across all pages.")
                .integer("totalPages", "Total number of pages.")
                .bool("hasNext", "True when another page is available.")
                .build();
    }

    public static ObjectNode listSchema(String itemDescription, ObjectNode itemSchema) {
        return McpJsonSchema.object()
                .objectArray("items", itemDescription, itemSchema, true)
                .integer("count", "Number of items returned.")
                .build();
    }

    public static ObjectNode acknowledgement(String field, String value) {
        ObjectNode node = FACTORY.objectNode();
        node.put(field, value);
        node.put("ok", true);
        return node;
    }

    public static ObjectNode acknowledgementSchema(String field, String description) {
        return McpJsonSchema.object()
                .string(field, description)
                .bool("ok", "True when the operation completed.")
                .build();
    }

    public static ObjectNode single(String key, ObjectNode value) {
        ObjectNode node = FACTORY.objectNode();
        node.set(key, value);
        return node;
    }

    public static ObjectNode singleSchema(String key, String description, ObjectNode valueSchema) {
        return McpJsonSchema.object()
                .nested(key, description, valueSchema, true)
                .build();
    }

    public static ObjectNode object() {
        return FACTORY.objectNode();
    }

    public static List<String> none() {
        return List.of();
    }

    // --- helpers ---------------------------------------------------------------

    /**
     * Rich text is stored as an HTML fragment. It is passed through rather than stripped:
     * a model reads the markup fine, and stripping would lose links and structure a user
     * asked about.
     */
    private static String richText(RichTextDto dto) {
        return Objects.isNull(dto) ? null : dto.getValue();
    }

    private static String reference(TaskDto dto) {
        if (Objects.isNull(dto.getTeam()) || Objects.isNull(dto.getTeamTagNo())) {
            return null;
        }
        return dto.getTeam().getTag() + "-" + dto.getTeamTagNo();
    }

    private static void putDate(ObjectNode node, String field, ZonedDateTime value) {
        node.put(field, Objects.isNull(value) ? null : DateTimeFormatter.ISO_INSTANT.format(value.toInstant()));
    }

    private static void putEnum(ObjectNode node, String field, Enum<?> value) {
        node.put(field, Objects.isNull(value) ? null : value.name());
    }
}
