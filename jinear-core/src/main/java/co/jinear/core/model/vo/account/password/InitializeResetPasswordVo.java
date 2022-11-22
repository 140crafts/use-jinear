package co.jinear.core.model.vo.account.password;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InitializeResetPasswordVo {

    private String email;
    private LocaleType preferredLocale;
}
