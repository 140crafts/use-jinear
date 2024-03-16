package co.jinear.core.service.account;

import co.jinear.core.model.dto.media.MediaDto;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.vo.media.RemoveMediaVo;
import co.jinear.core.repository.AccountRepository;
import co.jinear.core.service.media.MediaOperationService;
import co.jinear.core.service.media.MediaRetrieveService;
import co.jinear.core.service.notification.NotificationTargetOperationService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.reminder.ReminderOperationService;
import co.jinear.core.service.team.member.TeamMemberService;
import co.jinear.core.service.workspace.member.WorkspaceMemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountUpdateService {

    private final AccountRepository accountRepository;
    private final MediaOperationService mediaOperationService;
    private final MediaRetrieveService mediaRetrieveService;
    private final NotificationTargetOperationService notificationTargetOperationService;
    private final PassiveService passiveService;
    private final WorkspaceMemberService workspaceMemberService;
    private final TeamMemberService teamMemberService;
    private final ReminderOperationService reminderOperationService;

    @Transactional
    public void updateEmailConfirmed(String accountId, boolean emailConfirmed) {
        log.info("Update email confirmed has started accountId: {}, emailConfirmed: {}", accountId, emailConfirmed);
        accountRepository.updateEmailConfirmed(accountId, emailConfirmed);
    }

    @Transactional
    public void updateAccountLocale(String accountId, LocaleType localeType) {
        log.info("Update account locale has started. accountId: {}, localeType: {}", accountId, localeType);
        accountRepository.updateLocaleType(accountId, localeType);
    }

    @Transactional
    public void updateAccountTimeZone(String accountId, String timeZone) {
        log.info("Update account time zone has started. accountId: {}, timeZone: {}", accountId, timeZone);
        accountRepository.updateTimeZone(accountId, timeZone);
    }

    @Transactional
    public void anonymizeAccount(String accountId) {
        log.info("Anonymize account has started. accountId: {}", accountId);
        String passiveId = passiveService.createUserActionPassive(accountId);
        updateAccountGhostAndSetEmailAsEmpty(accountId);
        removeAccountProfilePictureIfPresent(accountId);
        updateAllNotificationTargetsAsPassive(accountId, passiveId);
        removeAccountFromAllWorkspacesAndTeams(accountId, passiveId);
        updateAllRemindersAsPassive(accountId, passiveId);
    }

    private void updateAllRemindersAsPassive(String accountId, String passiveId) {
        log.info("Update all reminders as passive has started.");
        reminderOperationService.passivizeReminderAndAllActiveJobsRelatedWithOwner(accountId, passiveId);
    }

    private void removeAccountFromAllWorkspacesAndTeams(String accountId, String passiveId) {
        log.info("Remove account from all workspaces and teams has started.");
        workspaceMemberService.removeAllMembershipsOfAnAccount(accountId, passiveId);
        teamMemberService.removeAllTeamMembershipsOfAnAccount(accountId, passiveId);
    }

    private void updateAllNotificationTargetsAsPassive(String accountId, String passiveId) {
        log.info("Update all notification targets as passive has started.");
        notificationTargetOperationService.updateAllAsPassiveWithAccountId(accountId, passiveId);
    }

    private void removeAccountProfilePictureIfPresent(String accountId) {
        log.info("Remove account profile picture if present has started. accountId: {}", accountId);
        mediaRetrieveService.retrieveProfilePictureOptional(accountId)
                .map(MediaDto::getMediaId)
                .map(mediaId -> new RemoveMediaVo(accountId, mediaId))
                .ifPresent(mediaOperationService::deleteMedia);
    }

    private void updateAccountGhostAndSetEmailAsEmpty(String accountId) {
        log.info("Update account ghost and set email as empty has started. accountId: {}", accountId);
        accountRepository.updateGhostAndEmailAsEmpty(accountId);
    }
}
