package co.jinear.core.service.workspace;

import co.jinear.core.model.dto.workspace.WorkspaceDisplayPreferenceDto;
import co.jinear.core.model.entity.workspace.WorkspaceDisplayPreference;
import co.jinear.core.repository.WorkspaceDisplayPreferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceDisplayPreferenceService {

    private final WorkspaceDisplayPreferenceRepository workspaceDisplayPreferenceRepository;
    private final ModelMapper modelMapper;

    public Optional<WorkspaceDisplayPreferenceDto> retrieveAccountPreferredWorkspace(String accountId) {
        log.info("Retrieve account prefered workspace has started. accountId: {}", accountId);
        return retrieveEntityByAccountId(accountId)
                .map(workspaceDisplayPreference -> modelMapper.map(workspaceDisplayPreference, WorkspaceDisplayPreferenceDto.class));
    }

    public WorkspaceDisplayPreferenceDto setAccountPreferredWorkspace(String accountId, String workspaceId) {
        log.info("Set account preferred workspace has started. accountId: {}, workspaceId: {}", accountId, workspaceId);
        WorkspaceDisplayPreference workspaceDisplayPreference = retrieveEntityByAccountId(accountId)
                .orElseGet(() -> initializeDefault(accountId, workspaceId));
        workspaceDisplayPreference.setAccountId(accountId);
        workspaceDisplayPreference.setPreferredWorkspaceId(workspaceId);
        WorkspaceDisplayPreference saved = workspaceDisplayPreferenceRepository.save(workspaceDisplayPreference);
        log.info("Account preferred workspace has been set.");
        return modelMapper.map(saved, WorkspaceDisplayPreferenceDto.class);
    }

    private Optional<WorkspaceDisplayPreference> retrieveEntityByAccountId(String accountId) {
        return workspaceDisplayPreferenceRepository.findByAccountIdAndPassiveIdIsNull(accountId);
    }

    private WorkspaceDisplayPreference initializeDefault(String accountId, String workspaceId) {
        WorkspaceDisplayPreference workspaceDisplayPreference = new WorkspaceDisplayPreference();
        workspaceDisplayPreference.setAccountId(accountId);
        workspaceDisplayPreference.setPreferredWorkspaceId(workspaceId);
        return workspaceDisplayPreference;
    }
}
