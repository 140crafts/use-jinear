package co.jinear.core.service.workspace.activity;

import co.jinear.core.model.entity.workspace.WorkspaceActivity;
import co.jinear.core.model.vo.workspace.WorkspaceActivityCreateVo;
import co.jinear.core.repository.WorkspaceActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceActivityService {

    private final WorkspaceActivityRepository workspaceActivityRepository;

    public void createWorkspaceActivity(WorkspaceActivityCreateVo workspaceActivityCreateVo) {
        log.info("Create workspace activity has started. workspaceActivityCreateVo: {}", workspaceActivityCreateVo);
        WorkspaceActivity workspaceActivity = new WorkspaceActivity();
        workspaceActivity.setWorkspaceId(workspaceActivityCreateVo.getWorkspaceId());
        workspaceActivity.setTeamId(workspaceActivityCreateVo.getTeamId());
        workspaceActivity.setTaskId(workspaceActivityCreateVo.getTaskId());
        workspaceActivity.setBoardId(workspaceActivityCreateVo.getBoardId());
        workspaceActivity.setType(workspaceActivityCreateVo.getType());
        workspaceActivity.setPerformedBy(workspaceActivityCreateVo.getPerformedBy());
        workspaceActivity.setRelatedObjectId(workspaceActivityCreateVo.getRelatedObjectId());
        workspaceActivity.setOldState(workspaceActivityCreateVo.getOldState());
        workspaceActivity.setNewState(workspaceActivityCreateVo.getNewState());
        workspaceActivityRepository.save(workspaceActivity);
        log.info("Create workspace activity has ended.");
    }
}
