package co.jinear.core.model.enumtype.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChannelVisibilityType {

    EVERYONE(0),
    MEMBERS_ONLY(1),
    PUBLIC_WITH_GUESTS(2);

    private final int value;
}
