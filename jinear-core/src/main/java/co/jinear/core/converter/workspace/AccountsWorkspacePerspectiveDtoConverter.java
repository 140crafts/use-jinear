package co.jinear.core.converter.workspace;

import co.jinear.core.model.dto.workspace.AccountsWorkspacePerspectiveDto;
import co.jinear.core.model.dto.workspace.DetailedWorkspaceMemberDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountsWorkspacePerspectiveDtoConverter {

    public AccountsWorkspacePerspectiveDto convert(DetailedWorkspaceMemberDto detailedWorkspaceMemberDto) {
        AccountsWorkspacePerspectiveDto accountsWorkspacePerspectiveDto = mapWorkspaceDto(detailedWorkspaceMemberDto);
        accountsWorkspacePerspectiveDto.setRole(detailedWorkspaceMemberDto.getRole());
        return accountsWorkspacePerspectiveDto;
    }

    private AccountsWorkspacePerspectiveDto mapWorkspaceDto(DetailedWorkspaceMemberDto detailedWorkspaceMemberDto) {
        WorkspaceDto workspaceDto = detailedWorkspaceMemberDto.getWorkspace();
        AccountsWorkspacePerspectiveDto accountsWorkspacePerspectiveDto = new AccountsWorkspacePerspectiveDto();
        accountsWorkspacePerspectiveDto.setWorkspaceId(workspaceDto.getWorkspaceId());
        accountsWorkspacePerspectiveDto.setTitle(workspaceDto.getTitle());
        accountsWorkspacePerspectiveDto.setDescription(workspaceDto.getDescription());
        accountsWorkspacePerspectiveDto.setTier(workspaceDto.getTier());
        accountsWorkspacePerspectiveDto.setUsername(workspaceDto.getUsername());
        accountsWorkspacePerspectiveDto.setSettings(workspaceDto.getSettings());
        accountsWorkspacePerspectiveDto.setProfilePicture(workspaceDto.getProfilePicture());
        return accountsWorkspacePerspectiveDto;
    }
}
