package co.jinear.core.service.messaging.thread;

import co.jinear.core.model.entity.messaging.Thread;
import co.jinear.core.model.vo.messaging.message.InitializeMessageVo;
import co.jinear.core.repository.messaging.ThreadRepository;
import co.jinear.core.service.messaging.message.MessageOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThreadOperationService {

    private final ThreadRepository threadRepository;
    private final MessageOperationService messageOperationService;

    public void initializeThread(String ownerId, String channelId, String initialMessageBody) {
        log.info("Initialize thread has started. ownerId: {}, channelId: {}", ownerId, channelId);
        Thread thread = initialize(ownerId, channelId);
        initializeFirstMessage(ownerId, initialMessageBody, thread);
    }

    private void initializeFirstMessage(String ownerId, String initialMessageBody, Thread saved) {
        InitializeMessageVo initializeMessageVo = new InitializeMessageVo();
        initializeMessageVo.setAccountId(ownerId);
        initializeMessageVo.setBody(initialMessageBody);
        initializeMessageVo.setThreadId(saved.getThreadId());
        messageOperationService.initialize(initializeMessageVo);
    }

    private Thread initialize(String ownerId, String channelId) {
        Thread thread = new Thread();
        thread.setOwnerId(ownerId);
        thread.setChannelId(channelId);
        thread.setLastActivityTime(ZonedDateTime.now());
        return threadRepository.save(thread);
    }
}
