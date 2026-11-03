package co.jinear.core.service.note;

import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.entity.note.NoteFts;
import co.jinear.core.model.vo.note.NoteFtsSearchVo;
import co.jinear.core.repository.note.NoteFtsSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteSearchService {

    private static final int PAGE_SIZE = 50;

    private final NoteFtsSearchRepository noteFtsSearchRepository;
    private final NoteRetrieveService noteRetrieveService;

    public Page<NoteDto> search(NoteFtsSearchVo noteFtsSearchVo) {
        Pageable pageable = PageRequest.of(noteFtsSearchVo.getPage(), PAGE_SIZE);
        if (StringUtils.isBlank(noteFtsSearchVo.getQuery())) {
            return Page.empty(pageable);
        }
        Page<NoteFts> ranked = noteFtsSearchRepository.search(
                noteFtsSearchVo.getQuery(),
                noteFtsSearchVo.getWorkspaceId(),
                noteFtsSearchVo.getNotebookId(),
                noteFtsSearchVo.getParentNoteId(),
                noteFtsSearchVo.getAccountId(),
                noteFtsSearchVo.isWorkspaceAdmin(),
                pageable);
        List<NoteDto> notes = retrieveInRankOrder(ranked);
        return new PageImpl<>(notes, pageable, ranked.getTotalElements());
    }

    private List<NoteDto> retrieveInRankOrder(Page<NoteFts> ranked) {
        List<String> noteIds = ranked.map(NoteFts::getNoteId).getContent();
        List<NoteDto> notes = noteRetrieveService.retrieveAll(noteIds);
        Map<String, NoteDto> notesById = notes.stream()
                .collect(Collectors.toMap(NoteDto::getNoteId, Function.identity()));
        return noteIds.stream()
                .map(notesById::get)
                .filter(Objects::nonNull)
                .toList();
    }
}
