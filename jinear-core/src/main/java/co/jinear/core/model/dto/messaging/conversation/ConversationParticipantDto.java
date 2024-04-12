package co.jinear.core.model.dto.messaging.conversation;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ConversationParticipantDto extends BaseDto {

    private String conversationParticipantId;
    private String conversationId;
    private String accountId;
    private ZonedDateTime lastCheck;
    private ZonedDateTime leftAt;
    private ZonedDateTime silentUntil;
    private ConversationDto conversation;
    private PlainAccountProfileDto account;
}
