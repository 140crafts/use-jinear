package co.jinear.core.service.workspace.member;

import co.jinear.core.converter.workspace.WorkspaceInvitationDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.workspace.WorkspaceInvitationDto;
import co.jinear.core.model.entity.workspace.WorkspaceInvitation;
import co.jinear.core.repository.WorkspaceInvitationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceInvitationRetrieveService {

    private final WorkspaceInvitationRepository workspaceInvitationRepository;
    private final WorkspaceInvitationDtoConverter workspaceInvitationDtoConverter;

    public WorkspaceInvitationDto retrieve(String workspaceInvitationId) {
        log.info("Retrieve workspace invitation has started. workspaceInvitationId: {}", workspaceInvitationId);
        return workspaceInvitationRepository.findByWorkspaceInvitationIdAndPassiveIdIsNull(workspaceInvitationId)
                .map(workspaceInvitationDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public WorkspaceInvitation retrieveEntity(String workspaceInvitationId) {
        log.info("Retrieve workspace invitation has started. workspaceInvitationId: {}", workspaceInvitationId);
        return workspaceInvitationRepository.findByWorkspaceInvitationIdAndPassiveIdIsNull(workspaceInvitationId)
                .orElseThrow(NotFoundException::new);
    }
}
