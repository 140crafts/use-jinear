package co.jinear.core.service.workspace.member;

import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.workspace.WorkspaceMemberDto;
import co.jinear.core.repository.WorkspaceMemberRepository;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.media.MediaRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceMemberListingService {

    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final AccountRetrieveService accountRetrieveService;
    private final MediaRetrieveService mediaRetrieveService;
    private final ModelMapper modelMapper;

    public List<WorkspaceMemberDto> retrieveWorkspaceMembersByAccountId(String accountId) {
        log.info("Retrieve account workspace members has started. accountId: {}", accountId);
        return workspaceMemberRepository.findAllByAccountIdAndPassiveIdIsNull(accountId)
                .stream()
                .map(workspaceMember -> modelMapper.map(workspaceMember, WorkspaceMemberDto.class))
                .toList();
    }

    public Page<WorkspaceMemberDto> retrieveWorkspaceMembers(String workspaceId, PageRequest pageRequest) {
        log.info("Retrieve workspace members has started. workspaceId: {}, pageRequest: {}", workspaceId, pageRequest);
        return workspaceMemberRepository.findAllByWorkspaceIdAndPassiveIdIsNull(workspaceId, pageRequest)
                .map(workspaceMember -> modelMapper.map(workspaceMember, WorkspaceMemberDto.class));
    }

    public Page<WorkspaceMemberDto> retrieveWorkspaceMembersDetailed(String workspaceId, PageRequest pageRequest) {
        log.info("Retrieve workspace members has started. workspaceId: {}, pageRequest: {}", workspaceId, pageRequest);
        return workspaceMemberRepository.findAllByWorkspaceIdAndPassiveIdIsNull(workspaceId, pageRequest)
                .map(workspaceMember -> modelMapper.map(workspaceMember, WorkspaceMemberDto.class))
                .map(workspaceMemberDto -> fillAccountDtoIfPresent(workspaceMemberDto));
    }

    private WorkspaceMemberDto fillAccountDtoIfPresent(WorkspaceMemberDto workspaceMemberDto) {
        log.info("Fill accountDto if present has started for workspaceMemberDto: {}", workspaceMemberDto);
        accountRetrieveService.retrieveOptional(workspaceMemberDto.getAccountId())
                .map(accountDto -> fillProfilePictureIfPresent(accountDto))
                .ifPresent(workspaceMemberDto::setAccountDto);
        return workspaceMemberDto;
    }

    private AccountDto fillProfilePictureIfPresent(AccountDto accountDto) {
        log.info("Fill accountDto profile picture if present has started for accountDto: {}", accountDto);
        mediaRetrieveService.retrieveProfilePictureOptional(accountDto.getAccountId()).ifPresent(accountDto::setProfilePicture);
        return accountDto;
    }
}
