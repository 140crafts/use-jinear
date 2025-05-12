package co.jinear.core.model.enumtype.team;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamMemberRoleType {

    ADMIN(0),
    MEMBER(1),
    GUEST(2);

    private final int value;
}
