package co.jinear.core.service.note;

import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.vo.note.NoteInitializeVo;
import co.jinear.core.repository.note.idempotency.NoteIdempotencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdempotentNoteCreateService {

    private final NoteOperationService noteOperationService;
    private final NoteIdempotencyRepository noteIdempotencyRepository;
    private final NoteRetrieveService noteRetrieveService;
    private final NoteLockService noteLockService;

    public NoteDto initialize(NoteInitializeVo noteInitializeVo) {
        noteLockService.retryableLockForNoteInit(noteInitializeVo);
        try{
            String actualNoteId = noteIdempotencyRepository.retrieveDraftsActualNoteId(noteInitializeVo);
            if (Objects.nonNull(actualNoteId)) {
                return noteRetrieveService.retrieve(actualNoteId);
            }
            NoteDto initializedNote = noteOperationService.initialize(noteInitializeVo);
            noteIdempotencyRepository.storeDraftsActualNoteId(noteInitializeVo, initializedNote.getNoteId());
            return initializedNote;
        }finally {
            noteLockService.unlockForNoteInit(noteInitializeVo);
        }
    }
}
