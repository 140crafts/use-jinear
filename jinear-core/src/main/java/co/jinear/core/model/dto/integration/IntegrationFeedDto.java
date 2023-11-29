package co.jinear.core.model.dto.integration;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IntegrationFeedDto {

    private List<FeedItemDto> feedItemList;
    private String nextPageToken;
}
