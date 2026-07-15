package co.jinear.core.service.note;

import co.jinear.core.converter.note.NoteDtoConverter;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.vo.note.NoteFilterVo;
import co.jinear.core.repository.note.NoteFilterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteFilterService {

    private final NoteFilterRepository noteFilterRepository;
    private final NoteDtoConverter noteDtoConverter;

    public Page<NoteDto> filter(NoteFilterVo noteFilterVo) {
        return noteFilterRepository.filter(noteFilterVo)
                .map(noteDtoConverter::convert);
    }
}
