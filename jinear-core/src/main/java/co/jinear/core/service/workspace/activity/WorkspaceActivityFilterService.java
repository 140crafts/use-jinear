package co.jinear.core.service.workspace.activity;

import co.jinear.core.model.dto.workspace.WorkspaceActivityDto;
import co.jinear.core.model.vo.workspace.WorkspaceActivityFilterVo;
import co.jinear.core.repository.WorkspaceActivityFilterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceActivityFilterService {

    private final WorkspaceActivityFilterRepository workspaceActivityFilterRepository;
    private final WorkspaceActivityDetailRetrieveService workspaceActivityDetailRetrieveService;

    public Page<WorkspaceActivityDto> filterActivities(WorkspaceActivityFilterVo workspaceActivityFilterVo) {
        log.info("Filter workspace activities has started. workspaceActivityFilterVo: {}", workspaceActivityFilterVo);
        return workspaceActivityFilterRepository.filterBy(workspaceActivityFilterVo)
                .map(workspaceActivityDetailRetrieveService::retrieveDetailsAndMap);
    }
}
