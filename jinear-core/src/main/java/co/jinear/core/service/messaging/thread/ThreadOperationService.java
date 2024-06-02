package co.jinear.core.service.messaging.thread;

import co.jinear.core.model.dto.messaging.message.RichMessageDto;
import co.jinear.core.model.entity.messaging.Thread;
import co.jinear.core.model.enumtype.messaging.ThreadType;
import co.jinear.core.model.vo.messaging.message.InitializeMessageVo;
import co.jinear.core.repository.messaging.ThreadRepository;
import co.jinear.core.service.messaging.message.MessageOperationService;
import jakarta.transaction.Transactional;
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

    public void initializeChannelFirstThread(String ownerId, String channelId) {
        log.info("Initialize thread has started. ownerId: {}, channelId: {}", ownerId, channelId);
        initialize(ownerId, channelId, ThreadType.CHANNEL_INITIAL);
    }

    @Transactional
    public RichMessageDto initializeThread(String ownerId, String channelId, String initialMessageBody, ThreadType threadType) {
        log.info("Initialize thread has started. ownerId: {}, channelId: {}", ownerId, channelId);
        Thread thread = initialize(ownerId, channelId, threadType);
        return initializeFirstMessage(ownerId, initialMessageBody, thread);
    }

    private RichMessageDto initializeFirstMessage(String ownerId, String initialMessageBody, Thread saved) {
        InitializeMessageVo initializeMessageVo = new InitializeMessageVo();
        initializeMessageVo.setAccountId(ownerId);
        initializeMessageVo.setBody(initialMessageBody);
        initializeMessageVo.setThreadId(saved.getThreadId());
        return messageOperationService.initialize(initializeMessageVo);
    }

    private Thread initialize(String ownerId, String channelId, ThreadType threadType) {
        Thread thread = new Thread();
        thread.setThreadType(threadType);
        thread.setOwnerId(ownerId);
        thread.setChannelId(channelId);
        thread.setLastActivityTime(ZonedDateTime.now());
        return threadRepository.save(thread);
    }
}
