package co.jinear.core.service.chat.thread;

import co.jinear.core.model.entity.chat.Thread;
import co.jinear.core.repository.chat.ThreadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThreadOperationService {

    private ThreadRepository threadRepository;

    public void initializeThread(String ownerId, String channelId) {
        log.info("Initialize thread has started. ownerId: {}, channelId: {}", ownerId, channelId);
        Thread thread = mapThread(ownerId, channelId);
        threadRepository.save(thread);
    }

    private Thread mapThread(String ownerId, String channelId) {
        Thread thread = new Thread();
        thread.setOwnerId(ownerId);
        thread.setChannelId(channelId);
        thread.setLastActivityTime(ZonedDateTime.now());
        return thread;
    }
}
