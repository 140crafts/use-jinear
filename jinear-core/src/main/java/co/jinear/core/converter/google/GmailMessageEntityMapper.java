package co.jinear.core.converter.google;

import co.jinear.core.model.entity.google.GmailMessage;
import co.jinear.core.system.gcloud.googleapis.model.GmailMessageVo;
import co.jinear.core.system.gcloud.googleapis.model.GoogleBatchMessageBody;
import co.jinear.core.system.gcloud.googleapis.model.GoogleBatchResponseHeader;
import co.jinear.core.system.gcloud.googleapis.model.GoogleMessagePart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
public class GmailMessageEntityMapper {

    private static final String TO = "To";
    private static final String FROM = "From";
    private static final String SUBJECT = "Subject";

    public GmailMessage mapToEntity(String gmailThreadId, GmailMessageVo gmailMessageVo) {
        Optional<String> to = getHeaderValue(gmailMessageVo, TO);
        Optional<String> from = getHeaderValue(gmailMessageVo, FROM);
        Optional<String> subject = getHeaderValue(gmailMessageVo, SUBJECT);
        Optional<String> body = getBody(gmailMessageVo);


        GmailMessage gmailMessage = new GmailMessage();
        gmailMessage.setGmailThreadId(gmailThreadId);
        gmailMessage.setGId(gmailMessageVo.getId());
        gmailMessage.setGThreadId(gmailMessageVo.getThreadId());
        gmailMessage.setGHistoryId(gmailMessageVo.getHistoryId());
        gmailMessage.setGInternalDate(gmailMessageVo.getInternalDate());
        gmailMessage.setSnippet(gmailMessageVo.getSnippet());
        to.ifPresent(gmailMessage::setTo);
        from.ifPresent(gmailMessage::setFrom);
        subject.ifPresent(gmailMessage::setSubject);
        body.ifPresent(gmailMessage::setBody);

        return gmailMessage;
    }

    private Optional<String> getBody(GmailMessageVo gmailMessageVo) {
        return Optional.of(gmailMessageVo)
                .map(GmailMessageVo::getPayload)
                .map(GoogleMessagePart::getBody)
                .map(GoogleBatchMessageBody::getData);
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
