package co.jinear.core.repository.note.idempotency;

import co.jinear.core.model.vo.note.NoteInitializeVo;

public interface NoteIdempotencyRepository {

    void storeDraftsActualNoteId(NoteInitializeVo noteInitializeVo, String noteId);

    String retrieveDraftsActualNoteId(NoteInitializeVo noteInitializeVo);
}
