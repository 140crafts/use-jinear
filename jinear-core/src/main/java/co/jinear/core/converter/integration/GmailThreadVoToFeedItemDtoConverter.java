package co.jinear.core.converter.integration;

import co.jinear.core.model.dto.integration.FeedItemDto;
import co.jinear.core.model.dto.integration.FeedItemMessage;
import co.jinear.core.model.dto.integration.FeedItemMessageData;
import co.jinear.core.system.gcloud.googleapis.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class GmailThreadVoToFeedItemDtoConverter {

    private static final String TO = "To";
    private static final String FROM = "From";
    private static final String SUBJECT = "Subject";

    public FeedItemDto convert(GmailThreadVo gmailThreadVo) {
        return convert(gmailThreadVo, true);
    }

    public FeedItemDto convert(GmailThreadVo gmailThreadVo, boolean skipBody) {
        return Optional.of(gmailThreadVo)
                .map(GmailThreadVo::getMessages)
                .map(messages -> messages.stream()
                        .map(gmailMessageVo -> convert(gmailMessageVo, skipBody))
                        .toList()
                )
                .map(feedItemMessages -> new FeedItemDto(null, gmailThreadVo.getSnippet(), gmailThreadVo.getId(), feedItemMessages))
                .orElse(null);
    }

    public FeedItemMessage convert(GmailMessageVo gmailMessageVo, boolean skipBody) {
        Optional<String> to = getHeaderValue(gmailMessageVo, TO);
        Optional<String> from = getHeaderValue(gmailMessageVo, FROM);
        Optional<String> subject = getHeaderValue(gmailMessageVo, SUBJECT);
        Optional<String> body = skipBody ? Optional.empty() : getBody(gmailMessageVo);

        List<FeedItemMessageData> feedItemMessageData = skipBody ? Collections.emptyList() : mapFeedItemMessageDataList(gmailMessageVo);

        FeedItemMessage feedItemMessage = new FeedItemMessage();
        feedItemMessage.setExternalId(gmailMessageVo.getId());
        feedItemMessage.setExternalGroupId(gmailMessageVo.getThreadId());
        feedItemMessage.setDetailDataList(feedItemMessageData);

        to.ifPresent(feedItemMessage::setTo);
        from.ifPresent(feedItemMessage::setFrom);
        subject.ifPresent(feedItemMessage::setSubject);
        body.ifPresent(feedItemMessage::setBody);
        return feedItemMessage;
    }

    private List<FeedItemMessageData> mapFeedItemMessageDataList(GmailMessageVo gmailMessageVo) {
        return Optional.of(gmailMessageVo)
                .map(GmailMessageVo::getPayload)
                .map(GoogleMessagePart::getParts)
                .map(googleMessageParts -> googleMessageParts
                        .stream()
                        .map(this::mapFeedItemMessageData)
                        .toList())
                .orElse(null);
    }

    private FeedItemMessageData mapFeedItemMessageData(GoogleMessagePart googleMessagePart) {
        if (Objects.isNull(googleMessagePart)) {
            return null;
        }
        FeedItemMessageData feedItemMessageData = new FeedItemMessageData();
        feedItemMessageData.setMimeType(googleMessagePart.getMimeType());
        feedItemMessageData.setData(googleMessagePart.getBody().getData());
        Optional.of(googleMessagePart)
                .map(GoogleMessagePart::getBody)
                .map(GoogleBatchMessageBody::getData)
                .map(body -> Base64.getUrlDecoder().decode(body))
                .map(String::new)
                .ifPresent(feedItemMessageData::setData);

        Optional.of(googleMessagePart)
                .map(GoogleMessagePart::getParts)
                .map(googleMessageParts -> googleMessageParts
                        .stream()
                        .map(this::mapFeedItemMessageData)
                        .toList())
                .ifPresent(feedItemMessageData::setParts);
        return feedItemMessageData;
    }

    private Optional<String> getBody(GmailMessageVo gmailMessageVo) {
        return Optional.of(gmailMessageVo)
                .map(GmailMessageVo::getPayload)
                .map(GoogleMessagePart::getBody)
                .map(GoogleBatchMessageBody::getData)
                .map(body -> Base64.getUrlDecoder().decode(body))
                .map(String::new);
    }

    private Optional<String> getHeaderValue(GmailMessageVo gmailMessageVo, String headerName) {
        return Optional.of(gmailMessageVo)
                .map(GmailMessageVo::getPayload)
                .map(GoogleMessagePart::getHeaders)
                .map(Collection::stream)
                .map(stream -> stream
                        .filter(googleBatchResponseHeader -> headerName.equals(googleBatchResponseHeader.getName()))
                        .findFirst())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(GoogleBatchResponseHeader::getValue);
    }
}
