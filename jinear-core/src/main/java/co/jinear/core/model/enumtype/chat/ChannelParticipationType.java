package co.jinear.core.model.enumtype.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChannelParticipationType {

    EVERYONE(0),
    ADMINS_CAN_START_CONVERSATION_EVERYONE_CAN_REPLY(1),
    READ_ONLY(2);

    private final int value;
}
