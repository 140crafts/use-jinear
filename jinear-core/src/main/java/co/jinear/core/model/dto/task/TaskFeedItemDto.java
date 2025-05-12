package co.jinear.core.model.dto.task;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.feed.FeedDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskFeedItemDto extends BaseDto {

    private String taskFeedItemId;
    private String taskId;
    private String feedId;
    private String feedItemId;
    private FeedDto feed;
}
