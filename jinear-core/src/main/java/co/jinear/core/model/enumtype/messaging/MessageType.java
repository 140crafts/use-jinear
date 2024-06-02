package co.jinear.core.model.enumtype.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {

    USER_MESSAGE(0),
    CONVERSATION_INIT(1);

    private final int value;
}
