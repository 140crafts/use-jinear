package co.jinear.core.model.vo.account;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
@ToString
public class AccountInitializeVo {
    private String email;
    private Boolean emailConfirmed = Boolean.FALSE;
    private String password;
    @Nullable
    private LocaleType preferredLocale;
}
