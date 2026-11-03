package co.jinear.core.note;

import co.jinear.core.converter.note.NoteSearchRequestToVoConverterImpl;
import co.jinear.core.manager.note.NoteSearchManager;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.entity.note.NoteFts;
import co.jinear.core.model.request.note.NoteSearchRequest;
import co.jinear.core.model.vo.note.NoteFtsSearchVo;
import co.jinear.core.repository.note.NoteFtsSearchRepository;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.note.NoteRetrieveService;
import co.jinear.core.service.note.NoteSearchService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;

class NoteSearchTest {

    private final NoteFtsSearchRepository noteFtsSearchRepository = Mockito.mock(NoteFtsSearchRepository.class);
    private final NoteRetrieveService noteRetrieveService = Mockito.mock(NoteRetrieveService.class);
    private final NoteSearchService noteSearchService = new NoteSearchService(noteFtsSearchRepository, noteRetrieveService);

    @Test
    void aBlankQueryReturnsAnEmptyPageWithoutAQuery() {
        NoteFtsSearchVo vo = searchVo("   ");

        Page<NoteDto> result = noteSearchService.search(vo);

        assertThat(result.getContent()).isEmpty();
        Mockito.verifyNoInteractions(noteFtsSearchRepository, noteRetrieveService);
    }

    @Test
    void notesComeBackInRankOrderFromOneBatchRead() {
        Pageable pageable = PageRequest.of(0, 50);
        Page<NoteFts> ranked = new PageImpl<>(List.of(fts("n3"), fts("n1"), fts("n2")), pageable, 3);
        Mockito.when(noteFtsSearchRepository.search(anyString(), anyString(), isNull(), isNull(), anyString(), anyBoolean(), any(Pageable.class)))
                .thenReturn(ranked);
        Mockito.when(noteRetrieveService.retrieveAll(List.of("n3", "n1", "n2")))
                .thenReturn(List.of(note("n1"), note("n2"), note("n3")));

        Page<NoteDto> result = noteSearchService.search(searchVo("plan"));

        assertThat(result.getContent()).extracting(NoteDto::getNoteId).containsExactly("n3", "n1", "n2");
        assertThat(result.getTotalElements()).isEqualTo(3);
        Mockito.verify(noteRetrieveService, Mockito.times(1)).retrieveAll(Mockito.anyList());
    }

    @Test
    void aNoteDeletedBetweenTheTwoReadsIsLeftOut() {
        Pageable pageable = PageRequest.of(0, 50);
        Page<NoteFts> ranked = new PageImpl<>(List.of(fts("n1"), fts("gone")), pageable, 2);
        Mockito.when(noteFtsSearchRepository.search(anyString(), anyString(), isNull(), isNull(), anyString(), anyBoolean(), any(Pageable.class)))
                .thenReturn(ranked);
        Mockito.when(noteRetrieveService.retrieveAll(List.of("n1", "gone")))
                .thenReturn(List.of(note("n1")));

        Page<NoteDto> result = noteSearchService.search(searchVo("plan"));

        assertThat(result.getContent()).extracting(NoteDto::getNoteId).containsExactly("n1");
    }

    @Test
    void managerPassesTheCallerAndTheWorkspaceAdminFlag() {
        SessionInfoService sessionInfoService = Mockito.mock(SessionInfoService.class);
        WorkspaceValidator workspaceValidator = Mockito.mock(WorkspaceValidator.class);
        NoteSearchService searchService = Mockito.mock(NoteSearchService.class);
        NoteSearchManager manager = new NoteSearchManager(sessionInfoService, workspaceValidator, searchService, new NoteSearchRequestToVoConverterImpl());
        Mockito.when(sessionInfoService.currentAccountId()).thenReturn("acc");
        Mockito.when(workspaceValidator.isWorkspaceAdminOrOwner("acc", "ws")).thenReturn(true);
        Mockito.when(searchService.search(any(NoteFtsSearchVo.class))).thenReturn(Page.empty());
        NoteSearchRequest request = new NoteSearchRequest();
        request.setQuery("plan");
        request.setNotebookId("nb");
        request.setParentNoteId("parent");

        manager.searchNotes("ws", request, 2);

        Mockito.verify(workspaceValidator).validateHasAccess("acc", "ws");
        ArgumentCaptor<NoteFtsSearchVo> captor = ArgumentCaptor.forClass(NoteFtsSearchVo.class);
        Mockito.verify(searchService).search(captor.capture());
        NoteFtsSearchVo vo = captor.getValue();
        assertThat(vo.getQuery()).isEqualTo("plan");
        assertThat(vo.getWorkspaceId()).isEqualTo("ws");
        assertThat(vo.getAccountId()).isEqualTo("acc");
        assertThat(vo.isWorkspaceAdmin()).isTrue();
        assertThat(vo.getPage()).isEqualTo(2);
        assertThat(vo.getNotebookId()).isEqualTo("nb");
        assertThat(vo.getParentNoteId()).isEqualTo("parent");
    }

    private NoteFtsSearchVo searchVo(String query) {
        NoteFtsSearchVo vo = new NoteFtsSearchVo();
        vo.setQuery(query);
        vo.setWorkspaceId("ws");
        vo.setAccountId("acc");
        return vo;
    }

    private NoteFts fts(String noteId) {
        NoteFts noteFts = new NoteFts();
        noteFts.setNoteId(noteId);
        return noteFts;
    }

    private NoteDto note(String noteId) {
        NoteDto noteDto = new NoteDto();
        noteDto.setNoteId(noteId);
        return noteDto;
    }
}
