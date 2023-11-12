package co.jinear.core.service.workspace.activity;

import co.jinear.core.converter.workspace.WorkspaceActivityConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.workspace.WorkspaceActivityDto;
import co.jinear.core.repository.WorkspaceActivityRepository;
import co.jinear.core.system.NormalizeHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceActivityRetrieveService {

    private final WorkspaceActivityRepository workspaceActivityRepository;
    private final WorkspaceActivityConverter workspaceActivityConverter;

    public WorkspaceActivityDto retrieveWorkspaceLatestActivity(String workspaceId, List<String> teamIdList) {
        log.info("Retrieve workspace latest activity has started. workspaceId: {}, teamIdList: {}", workspaceId, NormalizeHelper.listToString(teamIdList));
        return workspaceActivityRepository.findFirstByWorkspaceIdAndTeamIdIsInAndPassiveIdIsNullOrderByCreatedDateDesc(workspaceId, teamIdList)
                .map(workspaceActivityConverter::map)
                .orElseThrow(NotFoundException::new);
    }
}
