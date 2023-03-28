package co.jinear.core.model.dto.workspace;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceInvitationInfoDto extends BaseDto {

    private PlainAccountProfileDto accountDto;
    private WorkspaceDto workspaceDto;
    private WorkspaceInvitationDto invitationDto;
}
