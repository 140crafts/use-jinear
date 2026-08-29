package co.jinear.core.service.team.workflow;

import co.jinear.core.converter.team.TeamWorkflowStatusConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.entity.team.TeamWorkflowStatus;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import co.jinear.core.model.vo.team.workflow.InitializeTeamWorkflowStatusVo;
import co.jinear.core.repository.TeamWorkflowStatusRepository;
import co.jinear.core.service.passive.PassiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamWorkflowStatusService {

    private static final String CANNOT_REMOVE_LAST_MESSAGE_KEY = "workspace.team.workflow-status.cannot-remove-last";
    private static final String MAX_COUNT_EXCEEDED_MESSAGE_KEY = "workspace.team.workflow-status.max-count-exceeded";
    private static final String REORDER_LIST_MISMATCH_MESSAGE_KEY = "workspace.team.workflow-status.reorder-list-mismatch";

    private final int MAX_SAME_GROUP_STATUS_COUNT = 100;

    private final TeamWorkflowStatusRepository teamWorkflowStatusRepository;
    private final TeamWorkflowStatusRetrieveService teamWorkflowStatusRetrieveService;
    private final TeamWorkflowStatusLockService teamWorkflowStatusLockService;
    private final PassiveService passiveService;
    private final TeamWorkflowStatusConverter teamWorkflowStatusConverter;

    public TeamWorkflowStatusDto initializeTeamWorkflowStatus(InitializeTeamWorkflowStatusVo initializeTeamWorkflowStatusVo) {
        log.info("Initialize team workflow status has started. initializeTeamWorkflowStatusVo: {}", initializeTeamWorkflowStatusVo);
        validateStateGroupElementCount(initializeTeamWorkflowStatusVo.getWorkspaceId(), initializeTeamWorkflowStatusVo.getTeamId(), initializeTeamWorkflowStatusVo.getWorkflowStateGroup());
        teamWorkflowStatusLockService.lockTeamWorkflow(initializeTeamWorkflowStatusVo.getTeamId());
        try {
            int nextAvailableOrderNo = getNextAvailableOrderNo(initializeTeamWorkflowStatusVo);
            TeamWorkflowStatus teamWorkflowStatus = teamWorkflowStatusConverter.map(initializeTeamWorkflowStatusVo);
            teamWorkflowStatus.setOrder(nextAvailableOrderNo);
            TeamWorkflowStatus saved = teamWorkflowStatusRepository.saveAndFlush(teamWorkflowStatus);
            return teamWorkflowStatusConverter.map(saved);
        } catch (Exception e) {
            log.error("An error occurred while adding team workflow status.", e);
            throw e;
        } finally {
            teamWorkflowStatusLockService.unlockTeamWorkflow(initializeTeamWorkflowStatusVo.getTeamId());
        }
    }

    @Transactional
    public void removeTeamWorkflowStatus(String teamWorkflowStatusId, String performingAccountId) {
        log.info("Remove team workflow status has started. teamWorkflowStatusId: {}", teamWorkflowStatusId);
        TeamWorkflowStatus teamWorkflowStatus = teamWorkflowStatusRetrieveService.retrieveEntity(teamWorkflowStatusId);
        validateStateGroupHasMoreThanOneElement(teamWorkflowStatus.getWorkspaceId(), teamWorkflowStatus.getTeamId(), teamWorkflowStatus.getWorkflowStateGroup());
        //todo add validation for does any active task in this status?
        teamWorkflowStatusLockService.lockTeamWorkflow(teamWorkflowStatus.getTeamId());
        try {
            String passiveId = passiveService.createUserActionPassive(performingAccountId);
            teamWorkflowStatus.setPassiveId(passiveId);
            teamWorkflowStatusRepository.save(teamWorkflowStatus);
            rearrangeOrder(teamWorkflowStatus.getWorkspaceId(), teamWorkflowStatus.getTeamId(), teamWorkflowStatus.getWorkflowStateGroup());
            log.info("Remove team workflow status has finished. teamWorkflowStatusId: {}, passiveId: {}", teamWorkflowStatusId, passiveId);
        } finally {
            teamWorkflowStatusLockService.unlockTeamWorkflow(teamWorkflowStatus.getTeamId());
        }
    }

    public void changeTeamWorkflowStatusOrder(String teamWorkflowStatusId, String replaceWithTeamWorkflowStatusId) {
        log.info("Replacing workflow status order. teamWorkflowStatusId: {}, replaceWithTeamWorkflowStatusId: {}", teamWorkflowStatusId, replaceWithTeamWorkflowStatusId);
        TeamWorkflowStatus teamWorkflowStatus = teamWorkflowStatusRetrieveService.retrieveEntity(teamWorkflowStatusId);
        TeamWorkflowStatus replaceWithTeamWorkflowStatus = teamWorkflowStatusRetrieveService.retrieveEntity(replaceWithTeamWorkflowStatusId);
        log.info("Current order: {} of teamWorkflowStatus: {}", teamWorkflowStatus.getOrder(), teamWorkflowStatus.getTeamWorkflowStatusId());
        log.info("Current order: {} of replaceWithTeamWorkflowStatus: {}", replaceWithTeamWorkflowStatus.getOrder(), replaceWithTeamWorkflowStatus.getTeamWorkflowStatusId());
        int order = teamWorkflowStatus.getOrder();
        teamWorkflowStatus.setOrder(replaceWithTeamWorkflowStatus.getOrder());
        replaceWithTeamWorkflowStatus.setOrder(order);
        teamWorkflowStatusRepository.save(teamWorkflowStatus);
        teamWorkflowStatusRepository.save(replaceWithTeamWorkflowStatus);
        log.info("Orders replaced.");
    }

    @Transactional
    public void reorderTeamWorkflowStatuses(String workspaceId, String teamId, TeamWorkflowStateGroup workflowStateGroup, List<String> orderedTeamWorkflowStatusIds) {
        log.info("Reorder team workflow statuses has started. teamId: {}, workflowStateGroup: {}", teamId, workflowStateGroup);
        teamWorkflowStatusLockService.lockTeamWorkflow(teamId);
        try {
            List<TeamWorkflowStatus> teamWorkflowStatuses = teamWorkflowStatusRetrieveService.retrieveAllFromSameStateGroup(workspaceId, teamId, workflowStateGroup);
            validateOrderedIdsMatchesStateGroup(teamWorkflowStatuses, orderedTeamWorkflowStatusIds);
            Map<String, TeamWorkflowStatus> teamWorkflowStatusMap = teamWorkflowStatuses.stream()
                    .collect(Collectors.toMap(TeamWorkflowStatus::getTeamWorkflowStatusId, Function.identity()));
            for (int i = 0; i < orderedTeamWorkflowStatusIds.size(); i++) {
                TeamWorkflowStatus teamWorkflowStatus = teamWorkflowStatusMap.get(orderedTeamWorkflowStatusIds.get(i));
                teamWorkflowStatus.setOrder(i);
                log.info("Reordered team workflow status. teamWorkflowStatusId: {}, order: {}", teamWorkflowStatus.getTeamWorkflowStatusId(), i);
            }
            teamWorkflowStatusRepository.saveAll(teamWorkflowStatuses);
            log.info("Reorder team workflow statuses has finished.");
        } finally {
            teamWorkflowStatusLockService.unlockTeamWorkflow(teamId);
        }
    }

    public void changeTeamWorkflowStatusName(String teamWorkflowStatusId, String newName) {
        log.info("Change team workflow status name has started. teamWorkflowStatusId: {}, newName: {}", teamWorkflowStatusId, newName);
        TeamWorkflowStatus teamWorkflowStatus = teamWorkflowStatusRetrieveService.retrieveEntity(teamWorkflowStatusId);
        teamWorkflowStatus.setName(newName);
        teamWorkflowStatusRepository.save(teamWorkflowStatus);
        log.info("Change team workflow status name has ended");
    }

    private int getNextAvailableOrderNo(InitializeTeamWorkflowStatusVo initializeTeamWorkflowStatusVo) {
        return teamWorkflowStatusRetrieveService.retrieveNextAvailableOrderNo(initializeTeamWorkflowStatusVo.getTeamId(), initializeTeamWorkflowStatusVo.getWorkflowStateGroup());
    }

    private void rearrangeOrder(String workspaceId, String teamId, TeamWorkflowStateGroup workflowStateGroup) {
        log.info("Rearrange team workflow status order has started");
        List<TeamWorkflowStatus> teamWorkflowStatuses = teamWorkflowStatusRetrieveService.retrieveAllFromSameStateGroup(workspaceId, teamId, workflowStateGroup);
        for (int i = 0; i < teamWorkflowStatuses.size(); i++) {
            TeamWorkflowStatus teamWorkflowStatus = teamWorkflowStatuses.get(i);
            teamWorkflowStatus.setOrder(i);
            log.info("Rearranged team workflow status. teamWorkflowStatusId: {}, order: {}", teamWorkflowStatus.getTeamWorkflowStatusId(), i);
        }
        teamWorkflowStatusRepository.saveAll(teamWorkflowStatuses);
    }

    private void validateOrderedIdsMatchesStateGroup(List<TeamWorkflowStatus> teamWorkflowStatuses, List<String> orderedTeamWorkflowStatusIds) {
        Set<String> existingIds = teamWorkflowStatuses.stream()
                .map(TeamWorkflowStatus::getTeamWorkflowStatusId)
                .collect(Collectors.toSet());
        Set<String> submittedIds = new HashSet<>(orderedTeamWorkflowStatusIds);
        if (submittedIds.size() != orderedTeamWorkflowStatusIds.size() || !existingIds.equals(submittedIds)) {
            log.info("Submitted workflow status order does not match the state group. existingIds: {}, orderedTeamWorkflowStatusIds: {}", existingIds, orderedTeamWorkflowStatusIds);
            throw new BusinessException(REORDER_LIST_MISMATCH_MESSAGE_KEY);
        }
    }

    private void validateStateGroupHasMoreThanOneElement(String workspaceId, String teamId, TeamWorkflowStateGroup workflowStateGroup) {
        long count = teamWorkflowStatusRetrieveService.teamWorkflowStatusStateGroupCount(workspaceId, teamId, workflowStateGroup);
        if (count <= 1) {
            throw new BusinessException(CANNOT_REMOVE_LAST_MESSAGE_KEY);
        }
    }

    private void validateStateGroupElementCount(String workspaceId, String teamId, TeamWorkflowStateGroup workflowStateGroup) {
        long count = teamWorkflowStatusRetrieveService.teamWorkflowStatusStateGroupCount(workspaceId, teamId, workflowStateGroup);
        if (count > MAX_SAME_GROUP_STATUS_COUNT) {
            throw new BusinessException(MAX_COUNT_EXCEEDED_MESSAGE_KEY);
        }
    }
}
