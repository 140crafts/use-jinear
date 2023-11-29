package co.jinear.core.service.integration.feed;

import co.jinear.core.model.dto.integration.FeedItemDto;
import co.jinear.core.model.dto.integration.IntegrationFeedDto;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;

public interface IntegrationFeedRetrieveStrategy {

    IntegrationProvider getProvider();

    IntegrationFeedDto retrieveFeed(String integrationInfoId, String pageToken);

    FeedItemDto retrieveFeedItem(String integrationInfoId, String threadId);
}
