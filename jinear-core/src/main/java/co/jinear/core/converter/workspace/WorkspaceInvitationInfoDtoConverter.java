package co.jinear.core.converter.workspace;

import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.dto.workspace.WorkspaceInvitationDto;
import co.jinear.core.model.dto.workspace.WorkspaceInvitationInfoDto;
import co.jinear.core.model.entity.workspace.WorkspaceInvitation;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkspaceInvitationInfoDtoConverter {

    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final AccountRetrieveService accountRetrieveService;
    private final WorkspaceInvitationDtoConverter workspaceInvitationDtoConverter;

    public WorkspaceInvitationInfoDto convert(WorkspaceInvitation invitation) {
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithId(invitation.getWorkspaceId());
        PlainAccountProfileDto accountDto = accountRetrieveService.retrievePlainAccountProfile(invitation.getInvitedBy());
        WorkspaceInvitationDto workspaceInvitationDto = workspaceInvitationDtoConverter.convert(invitation);
        return mapValues(workspaceDto, accountDto, workspaceInvitationDto);
    }

    private WorkspaceInvitationInfoDto mapValues(WorkspaceDto workspaceDto, PlainAccountProfileDto accountDto, WorkspaceInvitationDto workspaceInvitationDto) {
        WorkspaceInvitationInfoDto workspaceInvitationInfoDto = new WorkspaceInvitationInfoDto();
        workspaceInvitationInfoDto.setWorkspaceDto(workspaceDto);
        workspaceInvitationInfoDto.setAccountDto(accountDto);
        workspaceInvitationInfoDto.setInvitationDto(workspaceInvitationDto);
        return workspaceInvitationInfoDto;
    }
}
