package co.jinear.core.service.messaging.thread;

import co.jinear.core.converter.messaging.thread.ThreadDtoConverter;
import co.jinear.core.model.dto.messaging.message.RichMessageDto;
import co.jinear.core.model.dto.messaging.thread.ThreadDto;
import co.jinear.core.model.entity.messaging.Thread;
import co.jinear.core.model.vo.messaging.message.InitializeMessageVo;
import co.jinear.core.repository.messaging.ThreadRepository;
import co.jinear.core.service.messaging.message.MessageOperationService;
import co.jinear.core.service.messaging.message.MessageRetrieveService;
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
    private final MessageRetrieveService messageRetrieveService;
    private final ThreadDtoConverter threadDtoConverter;

    @Transactional
    public RichMessageDto initializeThread(String ownerId, String channelId, String initialMessageBody) {
        log.info("Initialize thread has started. ownerId: {}, channelId: {}", ownerId, channelId);
        Thread thread = initialize(ownerId, channelId);
        RichMessageDto richMessageDto = initializeFirstMessage(ownerId, initialMessageBody, thread);
        return mapThread(thread, richMessageDto);
    }

    private RichMessageDto mapThread(Thread thread, RichMessageDto richMessageDto) {
        ThreadDto threadDto = threadDtoConverter.convert(thread);
        richMessageDto.setThread(threadDto);
        return richMessageDto;
    }

    private RichMessageDto initializeFirstMessage(String ownerId, String initialMessageBody, Thread saved) {
        InitializeMessageVo initializeMessageVo = new InitializeMessageVo();
        initializeMessageVo.setAccountId(ownerId);
        initializeMessageVo.setBody(initialMessageBody);
        initializeMessageVo.setThreadId(saved.getThreadId());
        RichMessageDto initialized = messageOperationService.initialize(initializeMessageVo);
        return messageRetrieveService.retrieveRich(initialized.getMessageId());
    }

    private Thread initialize(String ownerId, String channelId) {
        Thread thread = new Thread();
        thread.setOwnerId(ownerId);
        thread.setChannelId(channelId);
        thread.setLastActivityTime(ZonedDateTime.now());
        return threadRepository.save(thread);
    }
}
