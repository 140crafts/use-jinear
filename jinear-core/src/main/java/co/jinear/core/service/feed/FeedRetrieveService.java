package co.jinear.core.service.feed;

import co.jinear.core.converter.feed.FeedDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.feed.FeedDto;
import co.jinear.core.repository.feed.FeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedRetrieveService {

    private final FeedRepository feedRepository;
    private final FeedDtoConverter feedDtoConverter;

    public FeedDto retrieveFeed(String feedId) {
        log.info("Retrieve feed has started. feedId: {}", feedId);
        return feedRepository.findByFeedIdAndPassiveIdIsNull(feedId)
                .map(feedDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public Boolean checkFeedExist(String workspaceId, String accountId, String integrationId) {
        log.info("Check feed exists has started. workspaceId: {}, accountId: {}, integrationId: {}", workspaceId, accountId, integrationId);
        return feedRepository.countAllByWorkspaceIdAndInitializedByAndIntegrationInfoIdAndPassiveIdIsNull(workspaceId,accountId,integrationId) > 0L;
    }
}
