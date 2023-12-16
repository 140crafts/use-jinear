package co.jinear.core.manager.feed;

import co.jinear.core.converter.feed.AddFeedMemberVoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.feed.FeedDto;
import co.jinear.core.model.dto.feed.FeedMemberDto;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.feed.FeedMemberListingResponse;
import co.jinear.core.model.response.feed.FeedMemberPaginatedResponse;
import co.jinear.core.model.vo.feed.AddFeedMemberVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.feed.FeedMemberOperationService;
import co.jinear.core.service.feed.FeedMemberRetrieveService;
import co.jinear.core.service.feed.FeedRetrieveService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.validator.feed.FeedAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedMemberManager {

    private final FeedMemberRetrieveService feedMemberRetrieveService;
    private final FeedMemberOperationService feedMemberOperationService;
    private final WorkspaceValidator workspaceValidator;
    private final SessionInfoService sessionInfoService;
    private final FeedAccessValidator feedAccessValidator;
    private final FeedRetrieveService feedRetrieveService;
    private final AddFeedMemberVoConverter addFeedMemberVoConverter;
    private final PassiveService passiveService;

    public FeedMemberListingResponse retrieveMemberFeeds(String workspaceId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("Retrieve member feeds has started. currentAccountId: {}", currentAccountId);
        List<FeedMemberDto> feedMemberDtos = feedMemberRetrieveService.retrieveAccountFeeds(currentAccountId, workspaceId);
        return mapResponse(feedMemberDtos);
    }

    public FeedMemberPaginatedResponse retrieveFeedMembers(String feedId, int page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        feedAccessValidator.validateHasFeedAccess(currentAccountId, feedId);
        log.info("Retrieve feed members has started for feedId: {}, page: {}", feedId, page);
        Page<FeedMemberDto> feedMemberDtos = feedMemberRetrieveService.findAllByFeedIdAndPassiveIdIsNull(feedId, page);
        return mapResponse(feedMemberDtos);
    }

    public BaseResponse addFeedMember(String feedId, String accountId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        feedAccessValidator.validateHasAdminAccess(currentAccountId, feedId);
        log.info("Add feed member has started. currentAccountId: {}, feedId: {}, accountId: {}", currentAccountId, feedId, accountId);
        FeedDto feedDto = feedRetrieveService.retrieveFeed(feedId);
        AddFeedMemberVo addFeedMemberVo = addFeedMemberVoConverter.map(feedDto, accountId);
        feedMemberOperationService.addFeedMember(addFeedMemberVo);
        return new BaseResponse();
    }

    public BaseResponse kickFeedMember(String feedId, String accountId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        feedAccessValidator.validateHasAdminAccess(currentAccountId, feedId);
        validateKickYourself(accountId, currentAccountId);
        log.info("Kick feed member has started. currentAccountId: {}, feedId: {}, accountId: {}", currentAccountId, feedId, accountId);
        String passiveId = passiveService.createUserActionPassive();
        feedMemberOperationService.removeFeedMember(feedId, accountId, passiveId);
        passiveService.assignOwnership(passiveId, accountId);
        return null;
    }

    private FeedMemberPaginatedResponse mapResponse(Page<FeedMemberDto> feedMemberDtos) {
        FeedMemberPaginatedResponse feedMemberPaginatedResponse = new FeedMemberPaginatedResponse();
        feedMemberPaginatedResponse.setFeedMemberDtoPage(new PageDto<>(feedMemberDtos));
        return feedMemberPaginatedResponse;
    }

    private FeedMemberListingResponse mapResponse(List<FeedMemberDto> feedMemberDtos) {
        FeedMemberListingResponse feedMemberListingResponse = new FeedMemberListingResponse();
        feedMemberListingResponse.setFeedMemberDtos(feedMemberDtos);
        return feedMemberListingResponse;
    }

    private void validateKickYourself(String accountId, String currentAccountId) {
        if (currentAccountId.equalsIgnoreCase(accountId)) {
            throw new BusinessException("feed.members.manage.cannot-kick-yourself");
        }
    }
}
