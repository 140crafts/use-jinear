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
public class FeedItemDto {

    @Nullable
    private String title;
    @Nullable
    private String text;
    @Nullable
    private String externalId;
    @Nullable
    private List<FeedItemMessage> messages;
}
