package co.jinear.core.repository.note.idempotency;

import co.jinear.core.model.dto.captcha.CaptchaChallengeDto;
import co.jinear.core.model.vo.note.NoteInitializeVo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(value = "mock.redis.enabled", havingValue = "true")
public class MockRedisNoteIdempotencyRepository implements NoteIdempotencyRepository {

    private static final ConcurrentHashMap<String, CaptchaChallengeDto> CAPTCHA_CHALLENGE_MAP = new ConcurrentHashMap<>();

    @Override
    public void storeDraftsActualNoteId(NoteInitializeVo noteInitializeVo, String noteId) {
    }

    @Override
    public String retrieveDraftsActualNoteId(NoteInitializeVo noteInitializeVo) {
        return "mock-note-id";
    }
}
