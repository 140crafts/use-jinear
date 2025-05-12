package co.jinear.core.converter.workspace;

import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.token.TokenDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.entity.workspace.WorkspaceInvitation;
import co.jinear.core.model.vo.mail.WorkspaceInvitationMailVo;
import co.jinear.core.model.vo.token.GenerateTokenVo;
import co.jinear.core.model.vo.workspace.WorkspaceInvitationInitializeVo;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.token.TokenService;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkspaceInvitationMailVoConverter {
    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final AccountRetrieveService accountRetrieveService;
    private final WorkspaceInvitationTokenConverter workspaceInvitationTokenConverter;
    private final TokenService tokenService;

    public WorkspaceInvitationMailVo convert(WorkspaceInvitationInitializeVo vo, WorkspaceInvitation saved) {
        String senderName = retrieveSenderName(vo);
        String workspaceTitle = retrieveWorkspaceTitle(vo);
        String uniqueToken = generateUniqueToken(saved);

        WorkspaceInvitationMailVo workspaceInvitationMailVo = new WorkspaceInvitationMailVo();
        workspaceInvitationMailVo.setEmail(vo.getEmail());
        workspaceInvitationMailVo.setPreferredLocale(vo.getPreferredLocale());
        workspaceInvitationMailVo.setSenderName(senderName);
        workspaceInvitationMailVo.setWorkspaceName(workspaceTitle);
        workspaceInvitationMailVo.setToken(uniqueToken);

        return workspaceInvitationMailVo;
    }

    private String generateUniqueToken(WorkspaceInvitation saved) {
        GenerateTokenVo generateTokenVo = workspaceInvitationTokenConverter.convert(saved.getWorkspaceInvitationId());
        TokenDto tokenDto = tokenService.generateToken(generateTokenVo);
        return tokenDto.getUniqueToken();
    }

    private String retrieveWorkspaceTitle(WorkspaceInvitationInitializeVo vo) {
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithId(vo.getWorkspaceId());
        return workspaceDto.getTitle();
    }

    private String retrieveSenderName(WorkspaceInvitationInitializeVo vo) {
        AccountDto accountDto = accountRetrieveService.retrieve(vo.getInvitedBy());
        return accountDto.getUsername();
    }

}
