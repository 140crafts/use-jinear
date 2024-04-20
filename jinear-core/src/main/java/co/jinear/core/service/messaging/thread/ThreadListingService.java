package co.jinear.core.service.messaging.thread;

import co.jinear.core.converter.messaging.thread.ThreadDtoConverter;
import co.jinear.core.model.dto.messaging.thread.ThreadDto;
import co.jinear.core.repository.messaging.ThreadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThreadListingService {

    private static final int PAGE_SIZE = 100;
    private final ThreadRepository threadRepository;
    private final ThreadDtoConverter threadDtoConverter;

    public Page<ThreadDto> listThreads(String channelId, ZonedDateTime before) {
        log.info("List threads has started. channelId: {}, before: {}", channelId, before);
        return threadRepository.findAllByChannelIdAndLastActivityTimeBeforeAndPassiveIdIsNullOrderByLastActivityTimeDesc(channelId, before, PageRequest.of(0, PAGE_SIZE))
                .map(threadDtoConverter::convert);
    }
}
