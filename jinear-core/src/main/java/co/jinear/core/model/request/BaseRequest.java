package co.jinear.core.model.request;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import lombok.*;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseRequest {
    @Nullable
    private LocaleType locale = LocaleType.EN;
    @Nullable
    private String conversationId;
}
