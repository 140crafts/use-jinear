package co.jinear.core.service.integration.feed;

import co.jinear.core.model.dto.integration.IntegrationFeedDto;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;

public interface IntegrationFeedRetrieveStrategy<T> {

    IntegrationProvider getProvider();

    IntegrationFeedDto<T> retrieveFeed(String integrationId, int page);
}
