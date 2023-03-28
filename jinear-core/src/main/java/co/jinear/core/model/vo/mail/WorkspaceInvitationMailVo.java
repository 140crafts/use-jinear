package co.jinear.core.model.vo.mail;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceInvitationMailVo {

    private String email;
    private LocaleType preferredLocale;
    private String senderName;
    private String workspaceName;
    private String token;
}
