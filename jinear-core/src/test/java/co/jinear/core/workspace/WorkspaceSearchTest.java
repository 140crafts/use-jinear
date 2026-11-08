package co.jinear.core.workspace;

import co.jinear.core.controller.workspace.WorkspaceSearchController;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.manager.material.MaterialListingManager;
import co.jinear.core.manager.note.NoteSearchManager;
import co.jinear.core.manager.task.TaskSearchManager;
import co.jinear.core.manager.workspace.WorkspaceSearchManager;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.workspace.WorkspaceSearchDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceSearchType;
import co.jinear.core.model.request.material.MaterialTextSearchRequest;
import co.jinear.core.model.request.note.NoteSearchRequest;
import co.jinear.core.model.request.task.TaskSearchRequest;
import co.jinear.core.model.request.workspace.WorkspaceSearchRequest;
import co.jinear.core.model.response.material.MaterialPaginatedResponse;
import co.jinear.core.model.response.note.NotePaginatedResponse;
import co.jinear.core.model.response.task.TaskSearchResponse;
import co.jinear.core.model.response.workspace.WorkspaceSearchResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WorkspaceSearchTest {

    private final SessionInfoService sessionInfoService = Mockito.mock(SessionInfoService.class);
    private final WorkspaceValidator workspaceValidator = Mockito.mock(WorkspaceValidator.class);
    private final TaskSearchManager taskSearchManager = Mockito.mock(TaskSearchManager.class);
    private final NoteSearchManager noteSearchManager = Mockito.mock(NoteSearchManager.class);
    private final MaterialListingManager materialListingManager = Mockito.mock(MaterialListingManager.class);
    private final WorkspaceSearchManager workspaceSearchManager = new WorkspaceSearchManager(
            sessionInfoService, workspaceValidator, taskSearchManager, noteSearchManager, materialListingManager);

    @BeforeEach
    void setUp() {
        Mockito.when(sessionInfoService.currentAccountId()).thenReturn("account-1");
        Mockito.when(taskSearchManager.searchTask(Mockito.any(), Mockito.anyInt())).thenReturn(taskResponse());
        Mockito.when(noteSearchManager.searchNotes(Mockito.anyString(), Mockito.any(), Mockito.anyInt())).thenReturn(noteResponse());
        Mockito.when(materialListingManager.searchText(Mockito.anyString(), Mockito.any(), Mockito.anyInt())).thenReturn(materialResponse());
    }

    private static WorkspaceSearchRequest request() {
        WorkspaceSearchRequest request = new WorkspaceSearchRequest();
        request.setWorkspaceId("ws-1");
        request.setQuery("launch plan");
        request.setTeamIdList(List.of("team-eng"));
        return request;
    }

    private static TaskSearchResponse taskResponse() {
        TaskDto task = new TaskDto();
        task.setTaskId("task-1");
        TaskSearchResponse response = new TaskSearchResponse();
        response.setResult(new PageDto<>(new PageImpl<>(List.of(task))));
        return response;
    }

    private static NotePaginatedResponse noteResponse() {
        NoteDto note = new NoteDto();
        note.setNoteId("note-1");
        NotePaginatedResponse response = new NotePaginatedResponse();
        response.setNoteDtoPageDto(new PageDto<>(new PageImpl<>(List.of(note))));
        return response;
    }

    private static MaterialPaginatedResponse materialResponse() {
        MaterialDto material = new MaterialDto();
        material.setMaterialId("material-1");
        MaterialPaginatedResponse response = new MaterialPaginatedResponse();
        response.setMaterialDtoPageDto(new PageDto<>(new PageImpl<>(List.of(material))));
        return response;
    }

    @Test
    void returnsTasksNotesAndMaterialsInOneResponse() {
        WorkspaceSearchDto result = workspaceSearchManager.search(request(), 0).getWorkspaceSearchDto();

        Mockito.verify(workspaceValidator).validateHasAccess("account-1", "ws-1");
        assertThat(result.getTasks().getContent()).extracting(TaskDto::getTaskId).containsExactly("task-1");
        assertThat(result.getNotes().getContent()).extracting(NoteDto::getNoteId).containsExactly("note-1");
        assertThat(result.getMaterials().getContent()).extracting(MaterialDto::getMaterialId).containsExactly("material-1");
    }

    @Test
    void passesTheQueryTeamsAndPageToEachSearch() {
        workspaceSearchManager.search(request(), 2);

        ArgumentCaptor<TaskSearchRequest> taskRequest = ArgumentCaptor.forClass(TaskSearchRequest.class);
        ArgumentCaptor<NoteSearchRequest> noteRequest = ArgumentCaptor.forClass(NoteSearchRequest.class);
        ArgumentCaptor<MaterialTextSearchRequest> materialRequest = ArgumentCaptor.forClass(MaterialTextSearchRequest.class);
        Mockito.verify(taskSearchManager).searchTask(taskRequest.capture(), Mockito.eq(2));
        Mockito.verify(noteSearchManager).searchNotes(Mockito.eq("ws-1"), noteRequest.capture(), Mockito.eq(2));
        Mockito.verify(materialListingManager).searchText(Mockito.eq("ws-1"), materialRequest.capture(), Mockito.eq(2));
        assertThat(taskRequest.getValue().getWorkspaceId()).isEqualTo("ws-1");
        assertThat(taskRequest.getValue().getQuery()).isEqualTo("launch plan");
        assertThat(taskRequest.getValue().getTeamIdList()).containsExactly("team-eng");
        assertThat(noteRequest.getValue().getQuery()).isEqualTo("launch plan");
        assertThat(noteRequest.getValue().getNotebookId()).isNull();
        assertThat(materialRequest.getValue().getQuery()).isEqualTo("launch plan");
        assertThat(materialRequest.getValue().getMaterialType()).isNull();
    }

    @Test
    void searchesOnlyTheRequestedTypesAtTheRequestedPage() {
        WorkspaceSearchRequest request = request();
        request.setTypes(List.of(WorkspaceSearchType.MATERIAL));

        WorkspaceSearchDto result = workspaceSearchManager.search(request, 2).getWorkspaceSearchDto();

        Mockito.verify(materialListingManager).searchText(Mockito.eq("ws-1"), Mockito.any(), Mockito.eq(2));
        Mockito.verifyNoInteractions(taskSearchManager, noteSearchManager);
        assertThat(result.getTasks()).isNull();
        assertThat(result.getNotes()).isNull();
        assertThat(result.getMaterials().getContent()).extracting(MaterialDto::getMaterialId).containsExactly("material-1");
    }

    @Test
    void searchesTwoTypesWhenTwoAreRequested() {
        WorkspaceSearchRequest request = request();
        request.setTypes(List.of(WorkspaceSearchType.NOTE, WorkspaceSearchType.TASK));

        WorkspaceSearchDto result = workspaceSearchManager.search(request, 0).getWorkspaceSearchDto();

        Mockito.verifyNoInteractions(materialListingManager);
        assertThat(result.getTasks()).isNotNull();
        assertThat(result.getNotes()).isNotNull();
        assertThat(result.getMaterials()).isNull();
    }

    @Test
    void anEmptyTypeListSearchesEverything() {
        WorkspaceSearchRequest request = request();
        request.setTypes(List.of());

        WorkspaceSearchDto result = workspaceSearchManager.search(request, 0).getWorkspaceSearchDto();

        assertThat(result.getTasks()).isNotNull();
        assertThat(result.getNotes()).isNotNull();
        assertThat(result.getMaterials()).isNotNull();
    }

    @Test
    void searchesNothingWithoutWorkspaceAccess() {
        Mockito.doThrow(new NoAccessException()).when(workspaceValidator).validateHasAccess("account-1", "ws-1");

        assertThatThrownBy(() -> workspaceSearchManager.search(request(), 0)).isInstanceOf(NoAccessException.class);
        Mockito.verifyNoInteractions(taskSearchManager, noteSearchManager, materialListingManager);
    }

    @Test
    void controllerWrapsTheSectionsInData() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new WorkspaceSearchController(workspaceSearchManager)).build();

        mockMvc.perform(post("/v1/workspace/search").param("page", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"workspaceId\":\"ws-1\",\"query\":\"launch\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tasks.content[0].taskId").value("task-1"))
                .andExpect(jsonPath("$.data.notes.content[0].noteId").value("note-1"))
                .andExpect(jsonPath("$.data.materials.content[0].materialId").value("material-1"));
        Mockito.verify(taskSearchManager).searchTask(Mockito.any(), Mockito.eq(1));
    }

    @Test
    void controllerReturnsNullForTheSectionsNotRequested() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new WorkspaceSearchController(workspaceSearchManager)).build();

        mockMvc.perform(post("/v1/workspace/search").param("page", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"workspaceId\":\"ws-1\",\"query\":\"launch\",\"types\":[\"MATERIAL\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tasks").value(nullValue()))
                .andExpect(jsonPath("$.data.notes").value(nullValue()))
                .andExpect(jsonPath("$.data.materials.content[0].materialId").value("material-1"));
        Mockito.verify(materialListingManager).searchText(Mockito.eq("ws-1"), Mockito.any(), Mockito.eq(1));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"workspaceId\":\"ws-1\",\"query\":\"launch\",\"types\":[\"PROJECT\"]}",
            "{\"workspaceId\":\"ws-1\",\"query\":\"launch\",\"types\":[null]}"
    })
    void controllerRefusesAnUnknownOrNullType(String body) throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new WorkspaceSearchController(workspaceSearchManager)).build();

        mockMvc.perform(post("/v1/workspace/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
        Mockito.verifyNoInteractions(workspaceValidator, taskSearchManager, noteSearchManager, materialListingManager);
    }

    @Test
    void controllerRefusesABlankQuery() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new WorkspaceSearchController(workspaceSearchManager)).build();

        mockMvc.perform(post("/v1/workspace/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"workspaceId\":\"ws-1\",\"query\":\" \"}"))
                .andExpect(status().isBadRequest());
        Mockito.verifyNoInteractions(workspaceValidator, taskSearchManager);
    }
}
