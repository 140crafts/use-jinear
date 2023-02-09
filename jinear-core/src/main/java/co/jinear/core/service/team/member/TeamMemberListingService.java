package co.jinear.core.service.team.member;

import co.jinear.core.converter.team.TeamMemberConverter;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.repository.TeamMemberRepository;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.media.MediaRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamMemberListingService {

    private final int PAGE_SIZE = 500;

    private final TeamMemberRepository teamMemberRepository;
    private final AccountRetrieveService accountRetrieveService;
    private final MediaRetrieveService mediaRetrieveService;
    private final TeamMemberConverter teamMemberConverter;

    public Page<TeamMemberDto> retrieveTeamMembers(String teamId, int page) {
        log.info("Retrieve team members has started. teamId: {}, page: {}", teamId, page);
        return teamMemberRepository.findAllByTeamIdAndPassiveIdIsNullOrderByCreatedDateAsc(teamId, PageRequest.of(page, PAGE_SIZE))
                .map(teamMemberConverter::map)
                .map(this::fillAccountDtoIfPresent);
    }

    private TeamMemberDto fillAccountDtoIfPresent(TeamMemberDto teamMemberDto) {
        log.info("Fill accountDto if present has started for teamMemberDto: {}", teamMemberDto);
        accountRetrieveService.retrieveOptional(teamMemberDto.getAccountId())
                .map(this::fillProfilePictureIfPresent)
                .ifPresent(teamMemberDto::setAccount);
        return teamMemberDto;
    }

    private AccountDto fillProfilePictureIfPresent(AccountDto accountDto) {
        log.info("Fill accountDto profile picture if present has started for accountDto: {}", accountDto);
        mediaRetrieveService.retrieveProfilePictureOptional(accountDto.getAccountId()).ifPresent(accountDto::setProfilePicture);
        return accountDto;
    }
}
