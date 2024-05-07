package co.jinear.core.model.enumtype.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChannelMemberRoleType {

    OWNER(0),
    ADMIN(1),
    MEMBER(2);

    private final int value;
}
