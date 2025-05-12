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
import java.util.Optional;

import static co.jinear.core.model.enumtype.messaging.ThreadType.INITIALIZED_BY_ROBOT;

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
        initializeMessageVo.setBody(initialMessageBody);
        initializeMessageVo.setThreadId(saved.getThreadId());
        Optional.of(saved)
                .map(Thread::getThreadType)
                .filter(INITIALIZED_BY_ROBOT::equals)
                .ifPresentOrElse(
                        threadType -> initializeMessageVo.setRobotId(ownerId),
                        () -> initializeMessageVo.setAccountId(ownerId));
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
