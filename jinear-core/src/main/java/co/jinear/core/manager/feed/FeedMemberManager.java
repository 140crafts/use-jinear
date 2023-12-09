package co.jinear.core.manager.feed;

import co.jinear.core.model.dto.feed.FeedMemberDto;
import co.jinear.core.model.response.feed.FeedMemberListingResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.feed.FeedMemberRetrieveService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedMemberManager {

    private final FeedMemberRetrieveService feedMemberRetrieveService;
    private final WorkspaceValidator workspaceValidator;
    private final SessionInfoService sessionInfoService;

    public FeedMemberListingResponse retrieveMemberFeeds(String workspaceId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("Retrieve member feeds has started. currentAccountId: {}", currentAccountId);
        List<FeedMemberDto> feedMemberDtos = feedMemberRetrieveService.retrieveAccountFeeds(currentAccountId, workspaceId);
        return mapResponse(feedMemberDtos);
    }

    private FeedMemberListingResponse mapResponse(List<FeedMemberDto> feedMemberDtos) {
        FeedMemberListingResponse feedMemberListingResponse = new FeedMemberListingResponse();
        feedMemberListingResponse.setFeedMemberDtos(feedMemberDtos);
        return feedMemberListingResponse;
    }
}
