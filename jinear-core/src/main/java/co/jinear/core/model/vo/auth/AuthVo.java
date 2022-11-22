package co.jinear.core.model.vo.auth;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthVo {
    private String email;
    private String csrf;
    private LocaleType preferredLocale;
    private String code;
}
