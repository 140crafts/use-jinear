package co.jinear.core.service.feed;

import co.jinear.core.model.entity.feed.FeedMember;
import co.jinear.core.model.vo.feed.AddFeedMemberVo;
import co.jinear.core.repository.feed.FeedMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedMemberOperationService {

    private final FeedMemberRepository feedMemberRepository;

    public void addFeedMember(AddFeedMemberVo addFeedMemberVo) {
        log.info("Add feed member has started. addFeedMemberVo: {}", addFeedMemberVo);
        FeedMember feedMember = mapToEntity(addFeedMemberVo);
        feedMemberRepository.save(feedMember);
        log.info("Add feed member has completed.");
    }

    private FeedMember mapToEntity(AddFeedMemberVo addFeedMemberVo) {
        FeedMember feedMember = new FeedMember();
        feedMember.setAccountId(addFeedMemberVo.getAccountId());
        feedMember.setWorkspaceId(addFeedMemberVo.getWorkspaceId());
        feedMember.setFeedId(addFeedMemberVo.getFeedId());
        return feedMember;
    }
}
