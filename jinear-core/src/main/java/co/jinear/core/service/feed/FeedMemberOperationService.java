package co.jinear.core.service.feed;

import co.jinear.core.model.entity.feed.FeedMember;
import co.jinear.core.model.vo.feed.AddFeedMemberVo;
import co.jinear.core.repository.feed.FeedMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedMemberOperationService {

    private final FeedMemberRepository feedMemberRepository;
    private final FeedMemberRetrieveService feedMemberRetrieveService;

    public void addFeedMember(AddFeedMemberVo addFeedMemberVo) {
        log.info("Add feed member has started. addFeedMemberVo: {}", addFeedMemberVo);
        FeedMember feedMember = mapToEntity(addFeedMemberVo);
        feedMemberRepository.save(feedMember);
        log.info("Add feed member has completed.");
    }

    public void removeFeedMember(String feedId, String accountId, String passiveId) {
        log.info("Remove feed member has started. feedId: {}, accountId: {}, passiveId: {}", feedId, accountId, passiveId);
        FeedMember feedMember = feedMemberRetrieveService.retrieveEntity(accountId, feedId);
        feedMember.setPassiveId(passiveId);
        feedMemberRepository.save(feedMember);
        log.info("Feed member removed with passiveId: {}", passiveId);
    }

    @Transactional
    public void removeAllMembers(String feedId, String passiveId) {
        log.info("Remove all members has started. feedId: {}, passiveId: {}", feedId, passiveId);
        feedMemberRepository.removeAllMembers(feedId, passiveId);
        log.info("All feed members removed with passiveId: {}", passiveId);
    }

    private FeedMember mapToEntity(AddFeedMemberVo addFeedMemberVo) {
        FeedMember feedMember = new FeedMember();
        feedMember.setAccountId(addFeedMemberVo.getAccountId());
        feedMember.setWorkspaceId(addFeedMemberVo.getWorkspaceId());
        feedMember.setFeedId(addFeedMemberVo.getFeedId());
        return feedMember;
    }
}
