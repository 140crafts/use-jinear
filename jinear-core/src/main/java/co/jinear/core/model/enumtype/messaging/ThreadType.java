package co.jinear.core.model.enumtype.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ThreadType {

    CLASSIC(0),
    CHANNEL_INITIAL(1),
    INITIALIZED_BY_ROBOT(2);

    private final int value;
}
