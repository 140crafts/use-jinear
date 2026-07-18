package co.jinear.core.service.note;

import co.jinear.core.exception.lock.LockedException;
import co.jinear.core.model.enumtype.lock.LockSourceType;
import co.jinear.core.model.vo.note.NoteInitializeVo;
import co.jinear.core.service.lock.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteLockService {

    private static final String NOTE_INIT_LOCK_KEY = "note-init:%s:%s:%s";
    private static final String NOTE_UPDATE_LOCK_KEY = "note-update:%s";

    private final LockService lockService;

    @Retryable(value = {LockedException.class}, maxAttempts = 8, backoff = @Backoff(delay = 200, multiplier = 2, maxDelay = 3000))
    public void retryableLockForNoteInit(NoteInitializeVo noteInitializeVo) {
        String noteInitLockKey = generateNoteInitLockKey(noteInitializeVo);
        lockService.lock(noteInitLockKey, LockSourceType.NOTE_INIT);
    }

    public void unlockForNoteInit(NoteInitializeVo noteInitializeVo) {
        String noteInitLockKey = generateNoteInitLockKey(noteInitializeVo);
        lockService.unlock(noteInitLockKey, LockSourceType.NOTE_INIT);
    }

    public void lockForUpdate(String noteId) {
        String key = generateNoteUpdateLockKey(noteId);
        lockService.lock(key, LockSourceType.NOTE_UPDATE);
    }

    public void unLockForUpdate(String noteId) {
        String key = generateNoteUpdateLockKey(noteId);
        lockService.unlock(key, LockSourceType.NOTE_UPDATE);
    }

    private String generateNoteInitLockKey(NoteInitializeVo noteInitializeVo) {
        String noteInitLockKey = NOTE_INIT_LOCK_KEY.formatted(noteInitializeVo.getWorkspaceId(), noteInitializeVo.getOwnerId(), noteInitializeVo.getConversationId());
        return noteInitLockKey;
    }

    private String generateNoteUpdateLockKey(String noteId) {
        return NOTE_UPDATE_LOCK_KEY.formatted(noteId);
    }
}
