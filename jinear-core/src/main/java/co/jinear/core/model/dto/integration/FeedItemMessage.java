package co.jinear.core.model.dto.integration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedItemMessage {

    @Nullable
    private String externalId;
    @Nullable
    private String externalGroupId;
    @Nullable
    private String from;
    @Nullable
    private String to;
    @Nullable
    private String subject;
    @Nullable
    private String body;
    @Nullable
    private ZonedDateTime date;
    @Nullable
    private List<FeedItemMessageData> detailDataList;
}
