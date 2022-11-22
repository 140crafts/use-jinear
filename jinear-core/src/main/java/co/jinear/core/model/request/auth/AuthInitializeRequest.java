package co.jinear.core.model.request.auth;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
@ToString
public class AuthInitializeRequest extends BaseRequest {
    private String email;
    @Nullable
    private LocaleType preferredLocale;
}
