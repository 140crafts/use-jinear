package co.jinear.core.service.feed;

import co.jinear.core.model.entity.feed.Feed;
import co.jinear.core.model.vo.feed.AddFeedMemberVo;
import co.jinear.core.model.vo.feed.InitializeFeedVo;
import co.jinear.core.repository.feed.FeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedOperationService {

    private final FeedRepository feedRepository;
    private final FeedMemberOperationService feedMemberOperationService;

    public void initializeFeed(InitializeFeedVo initializeFeedVo) {
        log.info("Initialize feed has started. initializeFeedVo: {}", initializeFeedVo);
        Feed feed = createFeed(initializeFeedVo);
        addInitialFeedMember(feed);
        log.info("Initialize feed has completed.");
    }

    private void addInitialFeedMember(Feed feed) {
        AddFeedMemberVo addFeedMemberVo = new AddFeedMemberVo();
        addFeedMemberVo.setAccountId(feed.getInitializedBy());
        addFeedMemberVo.setWorkspaceId(feed.getWorkspaceId());
        addFeedMemberVo.setFeedId(feed.getFeedId());
        feedMemberOperationService.addFeedMember(addFeedMemberVo);
    }

    private Feed createFeed(InitializeFeedVo initializeFeedVo) {
        Feed feed = mapToEntity(initializeFeedVo);
        return feedRepository.save(feed);
    }

    private Feed mapToEntity(InitializeFeedVo initializeFeedVo) {
        Feed feed = new Feed();
        feed.setWorkspaceId(initializeFeedVo.getWorkspaceId());
        feed.setInitializedBy(initializeFeedVo.getInitializedBy());
        feed.setIntegrationInfoId(initializeFeedVo.getIntegrationInfoId());
        feed.setName(initializeFeedVo.getName());
        return feed;
    }
}
