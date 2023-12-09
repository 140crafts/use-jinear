package co.jinear.core.model.dto.integration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailedFeedContentItemDto extends FeedContentItemDto {

    @Nullable
    private List<FeedItemMessage> messages;
}
