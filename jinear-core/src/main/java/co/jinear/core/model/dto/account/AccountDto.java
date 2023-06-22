package co.jinear.core.model.dto.account;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.media.MediaDto;
import co.jinear.core.model.dto.workspace.AccountsWorkspacePerspectiveDto;
import co.jinear.core.model.dto.workspace.WorkspaceDisplayPreferenceDto;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
public class AccountDto extends BaseDto {
    private String accountId;
    private String email;
    private Boolean emailConfirmed;
    private LocaleType localeType;
    private String timeZone;
    @Nullable
    private String username;
    private Set<AccountRoleDto> roles;
    @Nullable
    private MediaDto profilePicture;
    private List<AccountsWorkspacePerspectiveDto> workspaces;
    @Nullable
    private WorkspaceDisplayPreferenceDto workspaceDisplayPreference;
}
