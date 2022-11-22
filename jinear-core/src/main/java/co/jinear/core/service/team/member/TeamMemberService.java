package co.jinear.core.service.team.member;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.entity.team.TeamMember;
import co.jinear.core.model.enumtype.team.TeamAccountRoleType;
import co.jinear.core.model.vo.team.DeleteTeamMemberVo;
import co.jinear.core.model.vo.team.InitializeTeamMemberVo;
import co.jinear.core.repository.TeamMemberRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.system.NumberCompareHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final PassiveService passiveService;

    @Transactional
    public void initializeTeamMember(InitializeTeamMemberVo initializeTeamMemberVo) {
        log.info("Initialize team member has started. initializeTeamMemberVo: {}", initializeTeamMemberVo);
        validateAccountIsNotTeamMember(initializeTeamMemberVo.getAccountId(), initializeTeamMemberVo.getTeamId());
        createTeamRole(initializeTeamMemberVo);
        log.info("Initialize team member has finished.");
    }

    @Transactional
    public void deleteTeamMember(DeleteTeamMemberVo deleteTeamMemberVo) {
        log.info("Delete team member has started. deleteTeamMemberVo: {}", deleteTeamMemberVo);
        teamMemberRepository.findByAccountIdAndTeamIdAndPassiveIdIsNull(deleteTeamMemberVo.getAccountId(), deleteTeamMemberVo.getTeamId())
                .ifPresent(teamMember -> {
                    deleteMember(deleteTeamMemberVo, teamMember);
                });
    }

    public boolean isAccountTeamOwner(String accountId, String teamId) {
        return teamMemberRepository.countAllByAccountIdAndTeamIdAndRoleAndPassiveIdIsNull(accountId, teamId, TeamAccountRoleType.OWNER) > 0L;
    }

    public boolean isAccountTeamMember(String accountId, String teamId) {
        return teamMemberRepository.countAllByAccountIdAndTeamIdAndPassiveIdIsNull(accountId, teamId) > 0L;
    }

    public void validateAccountTeamMember(String accountId, String teamId) {
        if (Boolean.FALSE.equals(isAccountTeamMember(accountId, teamId))) {
            throw new BusinessException("team.not-a-member");
        }
    }

    public void validateAccountIsNotTeamMember(String accountId, String teamId) {
        if (Boolean.TRUE.equals(isAccountTeamMember(accountId, teamId))) {
            throw new BusinessException();
        }
    }

    public void validateAccountHasRoleInTeam(String accountId, String teamId, List<TeamAccountRoleType> roleTypes) {
        Long count = teamMemberRepository.countAllByAccountIdAndTeamIdAndRoleIsInAndPassiveIdIsNull(accountId, teamId, roleTypes);
        if (NumberCompareHelper.isEquals(count, 0)) {
            throw new BusinessException();
        }
    }

    public void validateAccountIsNotTeamOwner(String accountId, String teamId) {
        if (isAccountTeamOwner(accountId, teamId)) {
            throw new BusinessException();
        }
    }

    private void createTeamRole(InitializeTeamMemberVo initializeTeamMemberVo) {
        TeamMember teamMember = new TeamMember();
        teamMember.setTeamId(initializeTeamMemberVo.getTeamId());
        teamMember.setAccountId(initializeTeamMemberVo.getAccountId());
        teamMember.setRole(initializeTeamMemberVo.getRole());
        teamMemberRepository.save(teamMember);
    }

    private void deleteMember(DeleteTeamMemberVo deleteTeamMemberVo, TeamMember teamMember) {
        String passiveId = passiveService.createUserActionPassive(deleteTeamMemberVo.getAccountId());
        teamMember.setPassiveId(passiveId);
        teamMemberRepository.save(teamMember);
        log.info("Delete team member has finished. teamMemberId: {}, passiveId: {}", teamMember.getTeamMemberId(), passiveId);
    }
}