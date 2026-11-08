package co.jinear.core.model.dto.account;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class AccountContextDto {

    private String accountId;
    @Nullable
    private String username;
    private LocaleType localeType;
    private String timeZone;
    private ZonedDateTime now;
    @Nullable
    private String preferredWorkspaceId;
    @Nullable
    private String preferredTeamId;
    private List<AccountContextWorkspaceDto> workspaces;
}
