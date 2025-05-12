package co.jinear.core.model.vo.mail;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class LoginMailVo {

    private String email;
    private LocaleType preferredLocale;
    private String emailCode;
}
