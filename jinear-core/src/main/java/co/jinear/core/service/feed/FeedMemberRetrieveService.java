package co.jinear.core.service.feed;

import co.jinear.core.converter.feed.FeedMemberDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.feed.FeedMemberDto;
import co.jinear.core.model.entity.feed.FeedMember;
import co.jinear.core.repository.feed.FeedMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedMemberRetrieveService {

    private final int PAGE_SIZE = 50;

    private final FeedMemberRepository feedMemberRepository;
    private final FeedMemberDtoConverter feedMemberDtoConverter;

    public FeedMember retrieveEntity(String accountId, String feedId) {
        return feedMemberRepository.findFirstByAccountIdAndFeedIdAndPassiveIdIsNull(accountId, feedId)
                .orElseThrow(NotFoundException::new);
    }

    public List<FeedMemberDto> retrieveAccountFeeds(String accountId, String workspaceId) {
        log.info("Retrieve account feeds has started. accountId: {}, workspaceId: {}", accountId, workspaceId);
        return feedMemberRepository.findAllByAccountIdAndWorkspaceIdAndPassiveIdIsNull(accountId, workspaceId)
                .stream()
                .map(feedMemberDtoConverter::convert)
                .toList();
    }

    public Page<FeedMemberDto> findAllByFeedIdAndPassiveIdIsNull(String feedId, int page) {
        return feedMemberRepository.findAllByFeedIdAndPassiveIdIsNull(feedId, PageRequest.of(page, PAGE_SIZE))
                .map(feedMemberDtoConverter::convert);

    }

    public boolean isFeedMember(String accountId, String feedId) {
        log.info("Is feed member has started. accountId: {}, feedId: {}", accountId, feedId);
        return feedMemberRepository.findFirstByAccountIdAndFeedIdAndPassiveIdIsNull(accountId, feedId).isPresent();
    }
}
