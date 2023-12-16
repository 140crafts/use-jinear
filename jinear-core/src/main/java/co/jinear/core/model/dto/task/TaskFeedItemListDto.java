package co.jinear.core.model.dto.task;

import co.jinear.core.model.dto.integration.FeedContentItemDto;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

@Getter
@Setter
public class TaskFeedItemListDto {
    @Nullable
    private List<FeedContentItemDto> feedContentItemList;
    @Nullable
    private Long totalItemCount;
}
