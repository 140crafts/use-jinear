package co.jinear.core.service.workspace.member;

import co.jinear.core.converter.workspace.WorkspaceInvitationDtoConverter;
import co.jinear.core.model.dto.workspace.WorkspaceInvitationDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceInvitationStatusType;
import co.jinear.core.repository.WorkspaceInvitationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceInvitationListingService {

    private final int PAGE_SIZE = 10;

    private final WorkspaceInvitationRepository workspaceInvitationRepository;
    private final WorkspaceInvitationDtoConverter workspaceInvitationDtoConverter;

    public Page<WorkspaceInvitationDto> retrieveActiveInvitations(String workspaceId, int page) {
        log.info("Retrieve active workspace invitations has started. workspaceId: {}, page: {}", workspaceId, page);
        return workspaceInvitationRepository.findAllByWorkspaceIdAndStatusAndPassiveIdIsNullOrderByCreatedDateAsc(workspaceId, WorkspaceInvitationStatusType.WAITING_FOR_ANSWER, PageRequest.of(page, PAGE_SIZE))
                .map(workspaceInvitationDtoConverter::convert);
    }
}
