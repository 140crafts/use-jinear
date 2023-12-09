package co.jinear.core.service.integration.feed;

import co.jinear.core.model.dto.feed.FeedDto;
import co.jinear.core.model.dto.integration.FeedContentDto;
import co.jinear.core.model.dto.integration.FeedContentItemDto;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;

public interface IntegrationFeedRetrieveStrategy {

    IntegrationProvider getProvider();

    FeedContentDto retrieveFeed(FeedDto feedDto, String pageToken);

    FeedContentItemDto retrieveFeedItem(FeedDto feedDto, String threadId);
}
