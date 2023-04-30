package co.jinear.core.model.vo.mail;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GenericInfoWithSubInfoMailVo {

    private String email;
    private LocaleType preferredLocale;
    private String mailTitle;
    private String mailBodyTitle;
    private String mailBodyText;
    private String mailBodySubtext;
}
