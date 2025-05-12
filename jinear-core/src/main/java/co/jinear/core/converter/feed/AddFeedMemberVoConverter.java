package co.jinear.core.converter.feed;

import co.jinear.core.model.dto.feed.FeedDto;
import co.jinear.core.model.vo.feed.AddFeedMemberVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddFeedMemberVoConverter {

    AddFeedMemberVo map(FeedDto feedDto, String accountId);
}
