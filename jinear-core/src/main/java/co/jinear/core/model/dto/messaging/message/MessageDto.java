package co.jinear.core.model.dto.messaging.message;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.robot.RobotDto;
import co.jinear.core.model.enumtype.messaging.MessageType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
public class MessageDto extends BaseDto {

    private String messageId;
    private String accountId;
    private String robotId;
    private String richTextId;
    private MessageType messageType;
    @Nullable
    private String threadId;
    @Nullable
    private String conversationId;
    @ToString.Exclude
    private PlainAccountProfileDto account;
    @ToString.Exclude
    private RobotDto robot;
    @ToString.Exclude
    @Nullable
    private RichTextDto richText;
    @Nullable
    @ToString.Exclude
    private Set<MessageDataDto> messageData;
    @Nullable
    @ToString.Exclude
    private Set<MessageReactionDto> messageReactions;
}
