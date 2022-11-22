package co.jinear.core.model.request;

import lombok.*;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseRequest {
    @Nullable
    private String locale;
    @Nullable
    private String conversationId;
}
