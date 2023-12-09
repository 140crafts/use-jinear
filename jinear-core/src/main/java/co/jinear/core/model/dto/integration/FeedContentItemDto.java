package co.jinear.core.model.dto.integration;

import co.jinear.core.model.dto.feed.FeedDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedContentItemDto {

    @Nullable
    private Set<FeedItemParticipant> participants;
    @Nullable
    private String title;
    @Nullable
    private String text;
    @Nullable
    private String externalId;
    @Nullable
    private List<FeedItemMessage> messages;
    @Nullable
    private ZonedDateTime date;
    private FeedDto feed;
}
