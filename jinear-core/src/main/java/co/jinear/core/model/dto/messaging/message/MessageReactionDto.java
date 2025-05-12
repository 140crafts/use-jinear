package co.jinear.core.model.dto.messaging.message;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.enumtype.messaging.MessageReactionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageReactionDto extends BaseDto {

    private String messageReactionId;
    private String messageId;
    private String accountId;
    private MessageReactionType reactionType;
    private String unicode;
    private PlainAccountProfileDto account;
}
