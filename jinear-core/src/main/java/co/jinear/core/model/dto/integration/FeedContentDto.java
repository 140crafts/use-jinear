package co.jinear.core.model.dto.integration;

import co.jinear.core.model.dto.feed.FeedDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FeedContentDto {

    private List<FeedContentItemDto> feedItemList;
    private String nextPageToken;
    private FeedDto feed;
}
