package co.jinear.core.service.messaging.thread;

import co.jinear.core.converter.messaging.thread.ThreadDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.messaging.thread.PlainThreadDto;
import co.jinear.core.model.dto.messaging.thread.ThreadDto;
import co.jinear.core.repository.messaging.ThreadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThreadRetrieveService {

    private final ThreadRepository threadRepository;
    private final ThreadDtoConverter threadDtoConverter;

    public ThreadDto retrieve(String threadId) {
        log.info("Retrieve has started. threadId: {}", threadId);
        return threadRepository.findByThreadIdAndPassiveIdIsNull(threadId)
                .map(threadDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public PlainThreadDto retrievePlain(String threadId) {
        log.info("Retrieve plain has started. threadId: {}", threadId);
        return threadRepository.findByThreadIdAndPassiveIdIsNull(threadId)
                .map(threadDtoConverter::convertPlain)
                .orElseThrow(NotFoundException::new);
    }
}
