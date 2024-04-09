package co.jinear.core.service.messaging.thread;

import co.jinear.core.model.entity.messaging.Thread;
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

    private static final int PAGE_SIZE = 25;
    private final ThreadRepository threadRepository;

    public Page<Thread> listThread(String channelId, int page) {
        log.info("List threads has started. channelId: {}, page: {}", channelId, page);
        return threadRepository.findAllByChannelIdAndPassiveIdIsNullOrderByLastActivityTimeDesc(channelId, PageRequest.of(page, PAGE_SIZE));
    }
}
