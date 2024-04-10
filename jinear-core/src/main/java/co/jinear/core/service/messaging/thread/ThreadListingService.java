package co.jinear.core.service.messaging.thread;

import co.jinear.core.converter.messaging.thread.ThreadDtoConverter;
import co.jinear.core.model.dto.messaging.thread.ThreadDto;
import co.jinear.core.repository.messaging.ThreadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThreadListingService {

    private static final int PAGE_SIZE = 100;
    private final ThreadRepository threadRepository;
    private final ThreadDtoConverter threadDtoConverter;

    public Page<ThreadDto> listThreads(String channelId, int page) {
        log.info("List threads has started. channelId: {}, page: {}", channelId, page);
        return threadRepository.findAllByChannelIdAndPassiveIdIsNullOrderByLastActivityTimeDesc(channelId, PageRequest.of(page, PAGE_SIZE))
                .map(threadDtoConverter::convert);
    }
}
