package co.jinear.core.model.dto.messaging.message;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Set;

@Getter
@Setter
public class MessageDto extends BaseDto {

    private String messageId;
    private String accountId;
    private String richTextId;
    @Nullable
    private String threadId;
    @Nullable
    private String conversationId;
    private PlainAccountProfileDto account;
    private RichTextDto richText;
    @Nullable
    private Set<MessageDataDto> messageData;
    @Nullable
    private Set<MessageReactionDto> messageReactions;
}
