package co.jinear.core.service.team.member;

import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.team.TeamMemberDto;
import co.jinear.core.repository.TeamMemberRepository;
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
public class TeamMemberListingService {

    private final TeamMemberRepository teamMemberRepository;
    private final AccountRetrieveService accountRetrieveService;
    private final MediaRetrieveService mediaRetrieveService;
    private final ModelMapper modelMapper;

    public List<TeamMemberDto> retrieveTeamMembersByAccountId(String accountId) {
        log.info("Retrieve account team members has started. accountId: {}", accountId);
        return teamMemberRepository.findAllByAccountIdAndPassiveIdIsNull(accountId)
                .stream()
                .map(teamMember -> modelMapper.map(teamMember, TeamMemberDto.class))
                .toList();
    }

    public Page<TeamMemberDto> retrieveTeamMembers(String teamId, PageRequest pageRequest) {
        log.info("Retrieve team members has started. teamId: {}, pageRequest: {}", teamId, pageRequest);
        return teamMemberRepository.findAllByTeamIdAndPassiveIdIsNull(teamId, pageRequest)
                .map(teamMember -> modelMapper.map(teamMember, TeamMemberDto.class));
    }

    public Page<TeamMemberDto> retrieveTeamMembersDetailed(String teamId, PageRequest pageRequest) {
        log.info("Retrieve team members has started. teamId: {}, pageRequest: {}", teamId, pageRequest);
        return teamMemberRepository.findAllByTeamIdAndPassiveIdIsNull(teamId, pageRequest)
                .map(teamMember -> modelMapper.map(teamMember, TeamMemberDto.class))
                .map(teamMemberDto -> fillAccountDtoIfPresent(teamMemberDto));
    }

    private TeamMemberDto fillAccountDtoIfPresent(TeamMemberDto teamMemberDto) {
        log.info("Fill accountDto if present has started for teamMemberDto: {}", teamMemberDto);
        accountRetrieveService.retrieveOptional(teamMemberDto.getAccountId())
                .map(accountDto -> fillProfilePictureIfPresent(accountDto))
                .ifPresent(teamMemberDto::setAccountDto);
        return teamMemberDto;
    }

    private AccountDto fillProfilePictureIfPresent(AccountDto accountDto) {
        log.info("Fill accountDto profile picture if present has started for accountDto: {}", accountDto);
        mediaRetrieveService.retrieveProfilePictureOptional(accountDto.getAccountId()).ifPresent(accountDto::setProfilePicture);
        return accountDto;
    }
}
