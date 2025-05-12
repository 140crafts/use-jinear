package co.jinear.core.converter.feed;

import co.jinear.core.model.dto.feed.FeedDto;
import co.jinear.core.model.entity.feed.Feed;
import co.jinear.core.model.entity.integration.IntegrationInfo;
import co.jinear.core.model.entity.integration.IntegrationScope;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface FeedDtoConverter {

    @Mapping(source = "feed.integrationInfo.provider", target = "provider")
    FeedDto convert(Feed feed);

    @AfterMapping
    default void afterMap(@MappingTarget FeedDto feedDto, Feed feed) {
        mapScopes(feedDto, feed);
    }

    default void mapScopes(FeedDto feedDto, Feed feed) {
        Optional.of(feed)
                .map(Feed::getIntegrationInfo)
                .map(IntegrationInfo::getScopes)
                .map(integrationScopes -> integrationScopes
                        .stream()
                        .map(IntegrationScope::getScope)
                        .toList())
                .ifPresent(feedDto::setScopes);
    }
}
