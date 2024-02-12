package co.jinear.core.converter.integration;

import co.jinear.core.model.dto.feed.FeedDto;
import co.jinear.core.model.dto.integration.FeedContentItemDto;
import co.jinear.core.model.dto.integration.FeedItemMessage;
import co.jinear.core.model.dto.integration.FeedItemMessageData;
import co.jinear.core.model.dto.integration.FeedItemParticipant;
import co.jinear.core.system.gcloud.gmail.model.GmailThreadInfo;
import co.jinear.core.system.gcloud.googleapis.model.*;
import com.google.common.collect.Streams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class GmailThreadVoToFeedItemDtoConverter {

    private static final String TO = "To";
    private static final String FROM = "From";
    private static final String SUBJECT = "Subject";

    public FeedContentItemDto convert(String googleUserEmail, GmailThreadInfo gmailThreadInfo, FeedDto feedDto, GmailThreadVo gmailThreadVo, boolean skipBody) {
        List<FeedItemMessage> messages = Optional.of(gmailThreadVo)
                .map(GmailThreadVo::getMessages)
                .map(messageVoList ->
                        messageVoList.stream()
                                .map(gmailMessageVo -> convert(gmailMessageVo, skipBody))
                                .toList()
                ).orElseGet(Collections::emptyList);


        FeedContentItemDto feedContentItemDto = new FeedContentItemDto();
        feedContentItemDto.setMessages(messages);
        feedContentItemDto.setText(gmailThreadInfo.getSnippet());
        feedContentItemDto.setExternalId(gmailThreadVo.getId());
        feedContentItemDto.setFeed(feedDto);

        messages.stream().findFirst().map(FeedItemMessage::getSubject).ifPresent(feedContentItemDto::setTitle);
//        messages.stream().findFirst().map(FeedItemMessage::getDate).ifPresent(feedContentItemDto::setDate);
        Streams.findLast(messages.stream()).map(FeedItemMessage::getDate).ifPresent(feedContentItemDto::setDate);

        mapParticipants(googleUserEmail, messages, feedContentItemDto);

        return feedContentItemDto;
    }

    public FeedContentItemDto convert(FeedDto feedDto, GmailThreadVo gmailThreadVo, boolean skipBody) {
        List<FeedItemMessage> messages = Optional.of(gmailThreadVo)
                .map(GmailThreadVo::getMessages)
                .map(messageVoList ->
                        messageVoList.stream()
                                .map(gmailMessageVo -> convert(gmailMessageVo, skipBody))
                                .toList()
                ).orElseGet(Collections::emptyList);

        FeedContentItemDto feedContentItemDto = new FeedContentItemDto();
        feedContentItemDto.setMessages(messages);
        feedContentItemDto.setTitle(gmailThreadVo.getSnippet());
        feedContentItemDto.setExternalId(gmailThreadVo.getId());
        feedContentItemDto.setFeed(feedDto);

        return feedContentItemDto;
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
        feedItemMessage.setDate(parseDate(gmailMessageVo.getInternalDate()));

        to.map(this::parseParticipant).ifPresent(feedItemMessage::setTo);
        from.map(this::parseParticipant).ifPresent(feedItemMessage::setFrom);
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

    private FeedItemParticipant parseParticipant(String participant) {
        if (Objects.isNull(participant)) {
            return null;
        }
        int startOfMail = participant.lastIndexOf('<');
        int endOfMail = participant.lastIndexOf('>');
        if (startOfMail != -1 && endOfMail != -1) {
            String name = participant.substring(0, Math.max(startOfMail - 1, 0));
            String address = participant.substring(startOfMail + 1, endOfMail);
            return new FeedItemParticipant(name, address);
        }
        return new FeedItemParticipant(participant, null);
    }

    private ZonedDateTime parseDate(String epochDate) {
        if (Objects.isNull(epochDate)) {
            return null;
        }
        Instant i = Instant.ofEpochMilli(Long.parseLong(epochDate));
        return ZonedDateTime.ofInstant(i, ZoneOffset.UTC);
    }

    private void mapParticipants(String googleUserEmail, List<FeedItemMessage> messages, FeedContentItemDto feedContentItemDto) {
        Set<FeedItemParticipant> participants = new HashSet<>();
        messages.forEach(feedItemMessage -> {
            Optional.of(feedItemMessage)
                    .map(FeedItemMessage::getTo)
                    .filter(feedItemParticipant -> !googleUserEmail.equalsIgnoreCase(feedItemParticipant.getAddress()) && !googleUserEmail.equalsIgnoreCase(feedItemParticipant.getName()))
                    .ifPresent(participants::add);
            Optional.of(feedItemMessage)
                    .map(FeedItemMessage::getFrom)
                    .filter(feedItemParticipant -> !googleUserEmail.equalsIgnoreCase(feedItemParticipant.getAddress()) && !googleUserEmail.equalsIgnoreCase(feedItemParticipant.getName()))
                    .ifPresent(participants::add);
        });
        feedContentItemDto.setParticipants(participants);
    }
}
