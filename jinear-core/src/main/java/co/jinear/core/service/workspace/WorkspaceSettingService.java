package co.jinear.core.service.workspace;

import co.jinear.core.converter.workspace.WorkspaceSettingConverter;
import co.jinear.core.model.dto.workspace.WorkspaceSettingDto;
import co.jinear.core.model.entity.workspace.WorkspaceSetting;
import co.jinear.core.model.vo.workspace.WorkspaceSettingsInitializeVo;
import co.jinear.core.repository.WorkspaceSettingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceSettingService {

    private final WorkspaceSettingRepository workspaceSettingRepository;
    private final WorkspaceSettingConverter workspaceSettingConverter;

    public WorkspaceSettingDto initializeWorkspaceSettings(WorkspaceSettingsInitializeVo workspaceSettingsInitializeVo) {
        log.info("Initialize workspace settings has started. workspaceSettingsInitializeVo: {}", workspaceSettingsInitializeVo);
        WorkspaceSetting workspaceSetting = initialize(workspaceSettingsInitializeVo);
        return workspaceSettingConverter.map(workspaceSetting);
    }

    private WorkspaceSetting initialize(WorkspaceSettingsInitializeVo workspaceSettingsInitializeVo) {
        WorkspaceSetting workspaceSetting = new WorkspaceSetting();
        workspaceSetting.setWorkspaceId(workspaceSettingsInitializeVo.getWorkspaceId());
        workspaceSetting.setVisibility(workspaceSettingsInitializeVo.getVisibility());
        workspaceSetting.setJoinType(workspaceSettingsInitializeVo.getJoinType());
        return workspaceSettingRepository.save(workspaceSetting);
    }
}
