package co.jinear.core.validator.feed;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.feed.FeedDto;
import co.jinear.core.service.feed.FeedMemberRetrieveService;
import co.jinear.core.service.feed.FeedRetrieveService;
import co.jinear.core.service.workspace.member.WorkspaceMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedAccessValidator {

    private final FeedMemberRetrieveService feedMemberRetrieveService;
    private final FeedRetrieveService feedRetrieveService;
    private final WorkspaceMemberService workspaceMemberService;

    public void validateHasFeedAccess(String accountId, String feedId) {
        boolean isFeedMember = isFeedMember(accountId, feedId);
        if (Boolean.FALSE.equals(isFeedMember)) {
            throw new NoAccessException();
        }
    }

    public void validateHasAdminAccess(String accountId, String feedId) {
        boolean isOwner = isFeedOwner(accountId, feedId);
        boolean isWorkspaceAdmin = isFeedWorkspaceAdmin(accountId, feedId);
        if (!isOwner || !isWorkspaceAdmin) {
            throw new NoAccessException();
        }
    }

    private boolean isFeedMember(String accountId, String feedId) {
        return feedMemberRetrieveService.isFeedMember(accountId, feedId);
    }

    public boolean isFeedOwner(String accountId, String feedId) {
        FeedDto feedDto = feedRetrieveService.retrieveFeed(feedId);
        return feedDto.getInitializedBy().equals(accountId);
    }

    private boolean isFeedWorkspaceAdmin(String accountId, String feedId) {
        FeedDto feedDto = feedRetrieveService.retrieveFeed(feedId);
        String workspaceId = feedDto.getWorkspaceId();
        return workspaceMemberService.doesAccountHaveWorkspaceAdminAccess(accountId, workspaceId);
    }
}
