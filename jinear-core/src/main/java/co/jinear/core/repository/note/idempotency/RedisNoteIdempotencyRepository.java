package co.jinear.core.repository.note.idempotency;

import co.jinear.core.model.vo.note.NoteInitializeVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Objects;

@Slf4j
@Repository
@ConditionalOnProperty(value = "mock.redis.enabled", havingValue = "false", matchIfMissing = true)
@RequiredArgsConstructor
public class RedisNoteIdempotencyRepository implements NoteIdempotencyRepository {

    private static final long DRAFT_INITIALIZE_TTL = 30L;
    private static final String NOTES_DRAFT_IDEMPOTENCY_KEY = "notes:draft:%s:%s:%s";

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void storeDraftsActualNoteId(NoteInitializeVo noteInitializeVo, String noteId) {
        if (Objects.isNull(noteInitializeVo.getConversationId())) {
            return;
        }
        String key = generateDraftKey(noteInitializeVo);
        redisTemplate.opsForValue().set(key, noteId, Duration.ofDays(DRAFT_INITIALIZE_TTL));
    }

    @Override
    public String retrieveDraftsActualNoteId(NoteInitializeVo noteInitializeVo) {
        if (Objects.isNull(noteInitializeVo.getConversationId())) {
            return null;
        }
        String key = generateDraftKey(noteInitializeVo);
        return redisTemplate.opsForValue().get(key);
    }

    private String generateDraftKey(NoteInitializeVo noteInitializeVo) {
        return String.format(NOTES_DRAFT_IDEMPOTENCY_KEY, noteInitializeVo.getWorkspaceId(), noteInitializeVo.getOwnerId(), noteInitializeVo.getConversationId());
    }
}
