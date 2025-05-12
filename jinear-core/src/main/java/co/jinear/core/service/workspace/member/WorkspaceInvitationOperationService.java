package co.jinear.core.service.workspace.member;

import co.jinear.core.converter.workspace.*;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.token.TokenDto;
import co.jinear.core.model.dto.workspace.WorkspaceInvitationDto;
import co.jinear.core.model.dto.workspace.WorkspaceInvitationInfoDto;
import co.jinear.core.model.entity.workspace.WorkspaceInvitation;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.enumtype.team.TeamMemberRoleType;
import co.jinear.core.model.enumtype.workspace.WorkspaceInvitationStatusType;
import co.jinear.core.model.vo.account.AccountInitializeVo;
import co.jinear.core.model.vo.mail.WorkspaceInvitationMailVo;
import co.jinear.core.model.vo.team.member.TeamMemberAddVo;
import co.jinear.core.model.vo.workspace.InitializeWorkspaceMemberVo;
import co.jinear.core.model.vo.workspace.WorkspaceInvitationInitializeVo;
import co.jinear.core.model.vo.workspace.WorkspaceInvitationRespondVo;
import co.jinear.core.repository.WorkspaceInvitationRepository;
import co.jinear.core.service.account.AccountInitializeService;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.mail.MailService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.team.member.TeamMemberService;
import co.jinear.core.service.token.TokenService;
import co.jinear.core.service.workspace.WorkspaceDisplayPreferenceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static co.jinear.core.model.enumtype.token.TokenType.WORKSPACE_INVITATION;
import static co.jinear.core.model.enumtype.workspace.WorkspaceInvitationStatusType.ACCEPTED;
import static co.jinear.core.model.enumtype.workspace.WorkspaceInvitationStatusType.DECLINED;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceInvitationOperationService {

    private final WorkspaceInvitationRepository workspaceInvitationRepository;
    private final AccountRetrieveService accountRetrieveService;
    private final WorkspaceInvitationInitializeVoToEntityConverter workspaceInvitationInitializeVoToEntityConverter;
    private final MailService mailService;
    private final WorkspaceInvitationMailVoConverter workspaceInvitationMailVoConverter;
    private final WorkspaceInvitationDtoConverter workspaceInvitationDtoConverter;
    private final TokenService tokenService;
    private final WorkspaceInvitationRetrieveService workspaceInvitationRetrieveService;
    private final WorkspaceMemberService workspaceMemberService;
    private final TeamMemberService teamMemberService;
    private final AccountInitializeService accountInitializeService;
    private final InitializeWorkspaceMemberVoConverter initializeWorkspaceMemberVoConverter;
    private final WorkspaceInvitationInfoDtoConverter workspaceInvitationInfoDtoConverter;
    private final WorkspaceDisplayPreferenceService workspaceDisplayPreferenceService;
    private final PassiveService passiveService;

    @Transactional
    public WorkspaceInvitationDto initializeInvitation(WorkspaceInvitationInitializeVo vo) {
        log.info("Initialize invitation has started. vo: {}", vo);
        WorkspaceInvitation workspaceInvitation = workspaceInvitationInitializeVoToEntityConverter.convert(vo);
        setAccountIdIfExistingAccountPresent(vo, workspaceInvitation);
        WorkspaceInvitation saved = saveInvitation(workspaceInvitation);
        generateAndSendInvitationMail(vo, saved);
        return workspaceInvitationDtoConverter.convert(workspaceInvitation);
    }

    @Transactional
    public String deleteInvitation(String invitationId) {
        log.info("Delete invitation has started. invitationId: {}", invitationId);
        WorkspaceInvitation workspaceInvitation = workspaceInvitationRetrieveService.retrieveEntity(invitationId);
        String passiveId = passiveService.createUserActionPassive();
        passivizeInvitation(passiveId, workspaceInvitation);
        tokenService.retrieveValidTokenWithRelatedObject(invitationId, WORKSPACE_INVITATION)
                .ifPresent(tokenDto -> tokenService.passivizeToken(tokenDto.getTokenId(), passiveId));
        return passiveId;
    }

    public WorkspaceInvitationInfoDto retrieveInvitationInfo(String token) {
        log.info("Retrieve invitation info has started for token:");
        WorkspaceInvitation invitation = retrieveInvitationFromToken(token);
        return workspaceInvitationInfoDtoConverter.convert(invitation);
    }

    @Transactional
    public void decideAndFinalizeInvitation(WorkspaceInvitationRespondVo workspaceInvitationRespondVo) {
        log.info("Decide and finalize invitation has started. workspaceInvitationRespondVo: {}", workspaceInvitationRespondVo);
        final String token = workspaceInvitationRespondVo.getToken();
        WorkspaceInvitation invitation = retrieveInvitationFromToken(token);
        validateRespondingEmailIsSameWithInvitedEmail(workspaceInvitationRespondVo, invitation);
        Optional.of(workspaceInvitationRespondVo)
                .map(WorkspaceInvitationRespondVo::getAccepted)
                .filter(Boolean.TRUE::equals)
                .ifPresentOrElse(
                        accepted -> acceptInvitation(workspaceInvitationRespondVo, invitation),
                        () -> declineInvitation(invitation)
                );
    }

    private void acceptInvitation(WorkspaceInvitationRespondVo workspaceInvitationRespondVo, WorkspaceInvitation invitation) {
        updateWorkspaceInvitationStatus(invitation, ACCEPTED);
        String accountId = retrieveUpToDateAccountId(invitation, workspaceInvitationRespondVo.getPreferredLocale());
        InitializeWorkspaceMemberVo initializeWorkspaceMemberVo = initializeWorkspaceMemberVoConverter.convert(invitation, accountId);
        workspaceMemberService.initializeWorkspaceMember(initializeWorkspaceMemberVo);
        assignTeam(invitation, accountId);
        setPreferredTeam(invitation, accountId);
        //todo add workspace activity
    }

    private void declineInvitation(WorkspaceInvitation invitation) {
        updateWorkspaceInvitationStatus(invitation, DECLINED);
    }

    private WorkspaceInvitation updateWorkspaceInvitationStatus(WorkspaceInvitation invitation, WorkspaceInvitationStatusType status) {
        invitation.setStatus(status);
        return workspaceInvitationRepository.save(invitation);
    }

    private WorkspaceInvitation retrieveInvitationFromToken(String token) {
        log.info("Retrieving invitation from token has started. token: {}", token);
        TokenDto tokenDto = tokenService.retrieveValidToken(token, WORKSPACE_INVITATION);
        String invitationId = tokenDto.getRelatedObject();
        return workspaceInvitationRetrieveService.retrieveEntity(invitationId);
    }

    private void generateAndSendInvitationMail(WorkspaceInvitationInitializeVo vo, WorkspaceInvitation saved) {
        log.info("Send workspace invitation mail has started. workspaceInvitationInitializeVo: {}, invitationId: {}", vo, saved.getWorkspaceInvitationId());
        WorkspaceInvitationMailVo workspaceInvitationMailVo = workspaceInvitationMailVoConverter.convert(vo, saved);
        try {
            mailService.sendWorkspaceInvitationMail(workspaceInvitationMailVo);
        } catch (Exception e) {
            log.error("Workspace invitation mail send has failed.", e);
        }
    }

    private void setAccountIdIfExistingAccountPresent(WorkspaceInvitationInitializeVo vo, WorkspaceInvitation workspaceInvitation) {
        Optional<AccountDto> existingAccount = accountRetrieveService.retrieveByEmailOptional(vo.getEmail());
        existingAccount.map(AccountDto::getAccountId).ifPresent(workspaceInvitation::setAccountId);
    }

    private WorkspaceInvitation saveInvitation(WorkspaceInvitation workspaceInvitation) {
        WorkspaceInvitation saved = workspaceInvitationRepository.save(workspaceInvitation);
        log.info("Workspace invitation saved. workspaceInvitationId: {}", saved.getWorkspaceInvitationId());
        return saved;
    }

    private String retrieveUpToDateAccountId(WorkspaceInvitation invitation, LocaleType preferredLocale) {
        log.info("Retrieve up to date accountId has started. email: {}", invitation.getEmail());
        return Optional.of(invitation)
                .map(WorkspaceInvitation::getAccountId)
                .orElseGet(() -> retrieveAccountByEmailOrRegister(invitation, preferredLocale));
    }

    private String retrieveAccountByEmailOrRegister(WorkspaceInvitation invitation, LocaleType preferredLocale) {
        log.info("Retrieve account by email or register has started.");
        return accountRetrieveService.retrieveByEmailOptional(invitation.getEmail())
                .map(AccountDto::getAccountId)
                .orElseGet(() -> registerAccount(invitation, preferredLocale));
    }

    private String registerAccount(WorkspaceInvitation invitation, LocaleType preferredLocale) {
        log.info("Register account from workspace invitation accept has started. invitationId: {}, preferredLocale: {}", invitation.getWorkspaceInvitationId(), preferredLocale);
        AccountInitializeVo accountInitializeVo = new AccountInitializeVo();
        accountInitializeVo.setEmail(invitation.getEmail());
        accountInitializeVo.setEmailConfirmed(Boolean.TRUE);
        accountInitializeVo.setLocale(preferredLocale);
        AccountDto accountDto = accountInitializeService.initializeAccount(accountInitializeVo);
        return accountDto.getAccountId();
    }

    private void validateRespondingEmailIsSameWithInvitedEmail(WorkspaceInvitationRespondVo workspaceInvitationRespondVo, WorkspaceInvitation invitation) {
        final String currentAccountEmail = workspaceInvitationRespondVo.getCurrentAccountEmail();
        final String invitedEmail = invitation.getEmail();
        if (Objects.nonNull(currentAccountEmail) && Boolean.FALSE.equals(currentAccountEmail.equalsIgnoreCase(invitedEmail))) {
            throw new NoAccessException();
        }
    }

    private void assignTeam(WorkspaceInvitation invitation, String accountId) {
        TeamMemberAddVo teamMemberAddVo = new TeamMemberAddVo();
        teamMemberAddVo.setAccountId(accountId);
        teamMemberAddVo.setTeamId(invitation.getInitialTeamId());
        teamMemberAddVo.setRole(TeamMemberRoleType.MEMBER);
        teamMemberService.addTeamMember(teamMemberAddVo);
    }

    private void setPreferredTeam(WorkspaceInvitation invitation, String accountId) {
        workspaceDisplayPreferenceService.setAccountPreferredWorkspace(accountId, invitation.getWorkspaceId());
        workspaceDisplayPreferenceService.setAccountPreferredTeamId(accountId, invitation.getInitialTeamId());
    }

    private void passivizeInvitation(String passiveId, WorkspaceInvitation workspaceInvitation) {
        workspaceInvitation.setPassiveId(passiveId);
        workspaceInvitationRepository.save(workspaceInvitation);
    }
}
