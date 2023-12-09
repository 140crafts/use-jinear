package co.jinear.core.service.feed;

import co.jinear.core.converter.feed.FeedMemberDtoConverter;
import co.jinear.core.model.dto.feed.FeedMemberDto;
import co.jinear.core.repository.feed.FeedMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedMemberRetrieveService {

    private final FeedMemberRepository feedMemberRepository;
    private final FeedMemberDtoConverter feedMemberDtoConverter;

    public List<FeedMemberDto> retrieveAccountFeeds(String accountId, String workspaceId) {
        log.info("Retrieve account feeds has started. accountId: {}, workspaceId: {}", accountId, workspaceId);
        return feedMemberRepository.findAllByAccountIdAndWorkspaceIdAndPassiveIdIsNull(accountId, workspaceId)
                .stream()
                .map(feedMemberDtoConverter::convert)
                .toList();
    }
}
