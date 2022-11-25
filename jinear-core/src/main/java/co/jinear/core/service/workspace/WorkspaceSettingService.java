package co.jinear.core.service.workspace;

import co.jinear.core.model.dto.workspace.WorkspaceSettingDto;
import co.jinear.core.model.entity.workspace.WorkspaceSetting;
import co.jinear.core.model.vo.workspace.WorkspaceSettingsInitializeVo;
import co.jinear.core.repository.WorkspaceSettingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceSettingService {

    private final WorkspaceSettingRepository workspaceSettingRepository;
    private final ModelMapper modelMapper;

    public WorkspaceSettingDto initializeWorkspaceSettings(WorkspaceSettingsInitializeVo workspaceSettingsInitializeVo) {
        log.info("Initialize workspace settings has started. workspaceSettingsInitializeVo: {}", workspaceSettingsInitializeVo);
        WorkspaceSetting workspaceSetting = initialize(workspaceSettingsInitializeVo);
        return modelMapper.map(workspaceSetting, WorkspaceSettingDto.class);
    }

    private WorkspaceSetting initialize(WorkspaceSettingsInitializeVo workspaceSettingsInitializeVo) {
        WorkspaceSetting workspaceSetting = new WorkspaceSetting();
        workspaceSetting.setWorkspaceId(workspaceSettingsInitializeVo.getWorkspaceId());
        workspaceSetting.setVisibility(workspaceSettingsInitializeVo.getVisibility());
        workspaceSetting.setJoinType(workspaceSettingsInitializeVo.getJoinType());
        return workspaceSettingRepository.save(workspaceSetting);
    }
}
