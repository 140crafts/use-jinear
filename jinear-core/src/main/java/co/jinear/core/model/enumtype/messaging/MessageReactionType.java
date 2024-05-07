package co.jinear.core.model.enumtype.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageReactionType {

    UNICODE_EMOJI(0);

    private final int value;
}
