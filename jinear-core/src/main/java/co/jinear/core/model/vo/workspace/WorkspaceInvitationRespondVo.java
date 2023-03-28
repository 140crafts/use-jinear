package co.jinear.core.model.vo.workspace;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WorkspaceInvitationRespondVo {

    private String token;
    private Boolean accepted;
    private LocaleType preferredLocale;
    private String currentAccountEmail;
}
