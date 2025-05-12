package co.jinear.core.converter.feed;

import co.jinear.core.model.dto.feed.FeedMemberDto;
import co.jinear.core.model.entity.feed.FeedMember;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {FeedDtoConverter.class})
public interface FeedMemberDtoConverter {

    @Mapping(source = "account.username.username", target = "account.username")
    FeedMemberDto convert(FeedMember feedMember);
}
