package co.jinear.core.mcp;

import co.jinear.core.converter.mcp.McpActivityViewConverter;
import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.board.UpdateTaskBoardTool;
import co.jinear.core.manager.mcp.tool.checklist.CreateTaskChecklistTool;
import co.jinear.core.manager.mcp.tool.checklist.UpdateChecklistItemTool;
import co.jinear.core.manager.mcp.tool.notification.ListNotificationsTool;
import co.jinear.core.manager.mcp.tool.task.AddTaskRelationTool;
import co.jinear.core.manager.mcp.tool.task.CreateTaskTool;
import co.jinear.core.manager.mcp.tool.task.UpdateTaskTool;
import co.jinear.core.manager.notification.NotificationEventListingManager;
import co.jinear.core.manager.task.TaskBoardManager;
import co.jinear.core.manager.task.TaskChecklistItemManager;
import co.jinear.core.manager.task.TaskChecklistManager;
import co.jinear.core.manager.task.TaskInitializeManager;
import co.jinear.core.manager.task.TaskRelationManager;
import co.jinear.core.manager.task.TaskUpdateManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.task.ChecklistDto;
import co.jinear.core.model.dto.task.ChecklistItemDto;
import co.jinear.core.model.dto.task.PlainTaskBoardEntryDto;
import co.jinear.core.model.dto.task.RelatedTaskDto;
import co.jinear.core.model.dto.task.TaskBoardDto;
import co.jinear.core.model.dto.task.TaskCollaboratorDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.task.TaskRelationDto;
import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.dto.workspace.WorkspaceActivityDto;
import co.jinear.core.model.enumtype.task.TaskBoardStateType;
import co.jinear.core.model.enumtype.task.TaskRelationType;
import co.jinear.core.model.enumtype.workspace.WorkspaceActivityType;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.view.McpActivityView;
import co.jinear.core.model.mcp.view.McpChecklistEnvelopeView;
import co.jinear.core.model.mcp.view.McpTaskDetailView;
import co.jinear.core.model.mcp.view.McpTaskRelationEnvelopeView;
import co.jinear.core.model.request.task.ChecklistItemLabelRequest;
import co.jinear.core.model.request.task.InitializeChecklistRequest;
import co.jinear.core.model.request.task.TaskBoardUpdateDueDateRequest;
import co.jinear.core.model.request.task.TaskBoardUpdateStateRequest;
import co.jinear.core.model.request.task.TaskBoardUpdateTitleRequest;
import co.jinear.core.model.request.task.TaskCollaboratorUpdateRequest;
import co.jinear.core.model.request.task.TaskInitializeRequest;
import co.jinear.core.model.request.task.TaskRelationInitializeRequest;
import co.jinear.core.model.response.notification.NotificationEventListingResponse;
import co.jinear.core.model.response.task.RetrieveChecklistResponse;
import co.jinear.core.model.response.task.TaskRelationResponse;
import co.jinear.core.model.response.task.TaskResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class McpTaskWorkToolsTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    private final McpViewConverter viewConverter = new McpViewConverter();

    private McpToolArguments args(String json) throws Exception {
        return McpToolArguments.of(OBJECT_MAPPER.readTree(json), OBJECT_MAPPER, VALIDATOR);
    }

    private McpToolContext context() {
        return McpToolContext.builder().accountId("account-1").scopes(Set.of()).build();
    }

    @Test
    void updateTaskSetsTheTopicAndReplacesTheCollaborators() throws Exception {
        TaskUpdateManager manager = Mockito.mock(TaskUpdateManager.class);
        UpdateTaskTool tool = new UpdateTaskTool(manager);

        McpToolResult result = tool.call(context(), args("""
                {"taskId":"task-1","topicId":"topic-1","collaboratorIds":["a","b"]}
                """));

        assertThat(result.isError()).isFalse();
        Mockito.verify(manager).updateTaskTopic("task-1", "topic-1");
        ArgumentCaptor<TaskCollaboratorUpdateRequest> captor = ArgumentCaptor.forClass(TaskCollaboratorUpdateRequest.class);
        Mockito.verify(manager).updateTaskCollaborators(Mockito.eq("task-1"), captor.capture());
        assertThat(captor.getValue().getCollaboratorIds()).containsExactly("a", "b");
    }

    @Test
    void updateTaskRemovesTheTopicOnAnEmptyStringAndClearsCollaboratorsOnAnEmptyList() throws Exception {
        TaskUpdateManager manager = Mockito.mock(TaskUpdateManager.class);
        UpdateTaskTool tool = new UpdateTaskTool(manager);

        tool.call(context(), args("""
                {"taskId":"task-1","topicId":"","collaboratorIds":[]}
                """));

        Mockito.verify(manager).removeTaskTopic("task-1");
        ArgumentCaptor<TaskCollaboratorUpdateRequest> captor = ArgumentCaptor.forClass(TaskCollaboratorUpdateRequest.class);
        Mockito.verify(manager).updateTaskCollaborators(Mockito.eq("task-1"), captor.capture());
        assertThat(captor.getValue().getCollaboratorIds()).isEmpty();
    }

    @Test
    void createTaskPassesTheParentAndTheCollaborators() throws Exception {
        TaskInitializeManager manager = Mockito.mock(TaskInitializeManager.class);
        TaskResponse response = new TaskResponse();
        response.setTaskDto(new TaskDto());
        Mockito.when(manager.initializeTask(any(TaskInitializeRequest.class))).thenReturn(response);
        CreateTaskTool tool = new CreateTaskTool(manager, viewConverter);

        tool.call(context(), args("""
                {"workspaceId":"w","teamId":"t","title":"Child","parentTaskId":"parent-1","collaboratorIds":["a"]}
                """));

        ArgumentCaptor<TaskInitializeRequest> captor = ArgumentCaptor.forClass(TaskInitializeRequest.class);
        Mockito.verify(manager).initializeTask(captor.capture());
        assertThat(captor.getValue().getSubTaskOf()).isEqualTo("parent-1");
        assertThat(captor.getValue().getCollaboratorIds()).containsExactly("a");
    }

    @Test
    void taskDetailCarriesCollaboratorsRelationsChecklistsAndBoards() {
        TaskDto task = new TaskDto();
        task.setTaskId("task-1");
        task.setTaskCollaborators(Set.of(collaborator("a", "ali")));
        task.setRelatedIn(Set.of(relation("rel-1", "child-1", "Child task", "task-1")));
        task.setChecklists(Set.of(checklist()));
        task.setTaskBoardEntries(Set.of(boardEntry()));

        McpTaskDetailView view = viewConverter.taskDetail(task);

        assertThat(view.getCollaborators()).singleElement()
                .satisfies(collaborator -> assertThat(collaborator.getUsername()).isEqualTo("ali"));
        assertThat(view.getRelations()).isEmpty();
        assertThat(view.getRelatedIn()).singleElement().satisfies(relation -> {
            assertThat(relation.getRelationType()).isEqualTo("SUBTASK");
            assertThat(relation.getTaskTitle()).isEqualTo("Child task");
        });
        assertThat(view.getChecklists()).singleElement()
                .satisfies(checklist -> assertThat(checklist.getItems()).extracting("label").containsExactly("first", "second"));
        assertThat(view.getBoards()).singleElement().satisfies(board -> {
            assertThat(board.getTaskBoardEntryId()).isEqualTo("entry-1");
            assertThat(board.getTitle()).isEqualTo("Sprint 1");
        });
    }

    @Test
    void createTaskChecklistReturnsTheNewChecklist() throws Exception {
        TaskChecklistManager manager = Mockito.mock(TaskChecklistManager.class);
        RetrieveChecklistResponse response = new RetrieveChecklistResponse();
        response.setChecklistDto(checklist());
        Mockito.when(manager.initializeChecklist(any(InitializeChecklistRequest.class))).thenReturn(response);
        CreateTaskChecklistTool tool = new CreateTaskChecklistTool(manager, viewConverter);

        McpToolResult result = tool.call(context(), args("""
                {"taskId":"task-1","title":"Release","firstItemLabel":"first"}
                """));

        McpChecklistEnvelopeView view = (McpChecklistEnvelopeView) result.getStructuredContent();
        assertThat(view.getChecklist().getChecklistId()).isEqualTo("checklist-1");
        ArgumentCaptor<InitializeChecklistRequest> captor = ArgumentCaptor.forClass(InitializeChecklistRequest.class);
        Mockito.verify(manager).initializeChecklist(captor.capture());
        assertThat(captor.getValue().getInitialItemLabel()).isEqualTo("first");
    }

    @Test
    void updateChecklistItemChangesTheLabelAndTheCheckedState() throws Exception {
        TaskChecklistItemManager manager = Mockito.mock(TaskChecklistItemManager.class);
        UpdateChecklistItemTool tool = new UpdateChecklistItemTool(manager);

        tool.call(context(), args("""
                {"checklistItemId":"item-1","label":"New text","checked":true}
                """));

        ArgumentCaptor<ChecklistItemLabelRequest> captor = ArgumentCaptor.forClass(ChecklistItemLabelRequest.class);
        Mockito.verify(manager).updateLabel(Mockito.eq("item-1"), captor.capture());
        assertThat(captor.getValue().getLabel()).isEqualTo("New text");
        Mockito.verify(manager).updateCheckedStatus("item-1", true);
    }

    @Test
    void updateChecklistItemRefusesAnEmptyChange() throws Exception {
        TaskChecklistItemManager manager = Mockito.mock(TaskChecklistItemManager.class);
        UpdateChecklistItemTool tool = new UpdateChecklistItemTool(manager);

        McpToolResult result = tool.call(context(), args("{\"checklistItemId\":\"item-1\"}"));

        assertThat(result.isError()).isTrue();
        assertThat(result.getText()).isEqualTo("mcp.error.checklist-item.nothing-to-update");
        Mockito.verifyNoInteractions(manager);
    }

    @Test
    void updateTaskBoardCallsOneEndpointPerSuppliedField() throws Exception {
        TaskBoardManager manager = Mockito.mock(TaskBoardManager.class);
        UpdateTaskBoardTool tool = new UpdateTaskBoardTool(manager);

        tool.call(context(), args("""
                {"taskBoardId":"board-1","title":"Sprint 2","state":"CLOSED"}
                """));

        ArgumentCaptor<TaskBoardUpdateTitleRequest> title = ArgumentCaptor.forClass(TaskBoardUpdateTitleRequest.class);
        Mockito.verify(manager).updateTitle(title.capture());
        assertThat(title.getValue().getTaskBoardId()).isEqualTo("board-1");
        ArgumentCaptor<TaskBoardUpdateStateRequest> state = ArgumentCaptor.forClass(TaskBoardUpdateStateRequest.class);
        Mockito.verify(manager).updateState(state.capture());
        assertThat(state.getValue().getState()).isEqualTo(TaskBoardStateType.CLOSED);
        Mockito.verify(manager, Mockito.never()).updateDueDate(any());
        Mockito.verify(manager, Mockito.never()).updateColor(any());
    }

    @Test
    void updateTaskBoardClearsTheDueDateWithTheFlag() throws Exception {
        TaskBoardManager manager = Mockito.mock(TaskBoardManager.class);
        UpdateTaskBoardTool tool = new UpdateTaskBoardTool(manager);

        McpToolResult result = tool.call(context(), args("""
                {"taskBoardId":"board-1","clearDueDate":true}
                """));

        assertThat(result.isError()).isFalse();
        ArgumentCaptor<TaskBoardUpdateDueDateRequest> captor = ArgumentCaptor.forClass(TaskBoardUpdateDueDateRequest.class);
        Mockito.verify(manager).updateDueDate(captor.capture());
        assertThat(captor.getValue().getDueDate()).isNull();
    }

    @Test
    void updateTaskBoardRefusesADueDateTogetherWithTheClearFlag() throws Exception {
        TaskBoardManager manager = Mockito.mock(TaskBoardManager.class);
        UpdateTaskBoardTool tool = new UpdateTaskBoardTool(manager);

        McpToolResult result = tool.call(context(), args("""
                {"taskBoardId":"board-1","dueDate":"2026-12-01","clearDueDate":true}
                """));

        assertThat(result.getText()).isEqualTo("mcp.error.task-board.due-date-conflict");
        Mockito.verifyNoInteractions(manager);
    }

    @Test
    void addTaskRelationReturnsTheRelationId() throws Exception {
        TaskRelationManager manager = Mockito.mock(TaskRelationManager.class);
        TaskRelationResponse response = new TaskRelationResponse();
        response.setTaskRelationDto(relation("rel-9", "task-1", null, "task-2"));
        Mockito.when(manager.initializeTaskRelation(any(TaskRelationInitializeRequest.class))).thenReturn(response);
        AddTaskRelationTool tool = new AddTaskRelationTool(manager, viewConverter);

        McpToolResult result = tool.call(context(), args("""
                {"taskId":"task-1","relatedTaskId":"task-2","relationType":"SUBTASK"}
                """));

        McpTaskRelationEnvelopeView view = (McpTaskRelationEnvelopeView) result.getStructuredContent();
        assertThat(view.getRelation().getTaskRelationId()).isEqualTo("rel-9");
        ArgumentCaptor<TaskRelationInitializeRequest> captor = ArgumentCaptor.forClass(TaskRelationInitializeRequest.class);
        Mockito.verify(manager).initializeTaskRelation(captor.capture());
        assertThat(captor.getValue().getRelation()).isEqualTo(TaskRelationType.SUBTASK);
    }

    @Test
    void listNotificationsUsesTheTeamEndpointWhenATeamIsGiven() throws Exception {
        NotificationEventListingManager manager = Mockito.mock(NotificationEventListingManager.class);
        NotificationEventListingResponse response = new NotificationEventListingResponse();
        response.setEventDtoPage(new PageDto<>(Page.empty()));
        Mockito.when(manager.retrieveTeamNotifications("w", "t", 0)).thenReturn(response);
        ListNotificationsTool tool = new ListNotificationsTool(manager, viewConverter);

        tool.call(context(), args("{\"workspaceId\":\"w\",\"teamId\":\"t\"}"));

        Mockito.verify(manager).retrieveTeamNotifications("w", "t", 0);
        Mockito.verify(manager, Mockito.never()).retrieveNotifications(Mockito.anyString(), Mockito.anyInt());
        assertThat(tool.definition().getAnnotations().isReadOnlyHint()).isFalse();
    }

    @Test
    void activityShowsStatusNamesAndHidesBodyRevisionIds() {
        McpActivityViewConverter converter = new McpActivityViewConverter();
        WorkspaceActivityDto statusChange = activity(WorkspaceActivityType.TASK_UPDATE_WORKFLOW_STATUS, "status-1", "status-2");
        statusChange.setOldWorkflowStatusDto(status("Backlog"));
        statusChange.setNewWorkflowStatusDto(status("In progress"));
        WorkspaceActivityDto bodyEdit = activity(WorkspaceActivityType.EDIT_TASK_DESC, "richtext-1", "richtext-2");

        McpActivityView statusView = converter.activity(statusChange);
        McpActivityView bodyView = converter.activity(bodyEdit);

        assertThat(statusView.getOldValue()).isEqualTo("Backlog");
        assertThat(statusView.getNewValue()).isEqualTo("In progress");
        assertThat(statusView.getTaskTitle()).isEqualTo("[ENG-1] Ship it");
        assertThat(bodyView.getOldValue()).isNull();
        assertThat(bodyView.getNewValue()).isNull();
    }

    private TaskCollaboratorDto collaborator(String accountId, String username) {
        PlainAccountProfileDto profile = new PlainAccountProfileDto();
        profile.setUsername(username);
        TaskCollaboratorDto dto = new TaskCollaboratorDto();
        dto.setAccountId(accountId);
        dto.setCollaborator(profile);
        return dto;
    }

    private TaskRelationDto relation(String relationId, String taskId, String taskTitle, String relatedTaskId) {
        RelatedTaskDto task = new RelatedTaskDto();
        task.setTitle(taskTitle);
        TaskRelationDto dto = new TaskRelationDto();
        dto.setTaskRelationId(relationId);
        dto.setTaskId(taskId);
        dto.setRelatedTaskId(relatedTaskId);
        dto.setRelationType(TaskRelationType.SUBTASK);
        dto.setTask(task);
        return dto;
    }

    private ChecklistDto checklist() {
        ChecklistDto dto = new ChecklistDto();
        dto.setChecklistId("checklist-1");
        dto.setTitle("Release");
        dto.setChecklistItems(Set.of(item("second", 2000L), item("first", 1000L)));
        return dto;
    }

    private ChecklistItemDto item(String label, long createdAt) {
        ChecklistItemDto dto = new ChecklistItemDto();
        dto.setChecklistItemId(label);
        dto.setLabel(label);
        dto.setIsChecked(Boolean.FALSE);
        dto.setCreatedDate(new Date(createdAt));
        return dto;
    }

    private PlainTaskBoardEntryDto boardEntry() {
        TaskBoardDto board = new TaskBoardDto();
        board.setTitle("Sprint 1");
        PlainTaskBoardEntryDto dto = new PlainTaskBoardEntryDto();
        dto.setTaskBoardEntryId("entry-1");
        dto.setTaskBoardId("board-1");
        dto.setOrder(0);
        dto.setTaskBoard(board);
        return dto;
    }

    private WorkspaceActivityDto activity(WorkspaceActivityType type, String oldState, String newState) {
        WorkspaceActivityDto dto = new WorkspaceActivityDto();
        dto.setWorkspaceActivityId("activity-1");
        dto.setType(type);
        dto.setGroupTitle("[ENG-1] Ship it");
        dto.setOldState(oldState);
        dto.setNewState(newState);
        return dto;
    }

    private TeamWorkflowStatusDto status(String name) {
        TeamWorkflowStatusDto dto = new TeamWorkflowStatusDto();
        dto.setName(name);
        return dto;
    }
}
