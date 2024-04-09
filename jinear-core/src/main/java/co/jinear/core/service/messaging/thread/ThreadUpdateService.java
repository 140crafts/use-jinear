package co.jinear.core.service.messaging.thread;

import co.jinear.core.repository.messaging.ThreadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThreadUpdateService {

    private final ThreadRepository threadRepository;

    public void updateLastActivityTimeAsNow(String threadId) {
        log.info("Update last activity time as now has started. threadId: {}", threadId);
        threadRepository.updateLastActivityTime(threadId, ZonedDateTime.now());
    }
}
