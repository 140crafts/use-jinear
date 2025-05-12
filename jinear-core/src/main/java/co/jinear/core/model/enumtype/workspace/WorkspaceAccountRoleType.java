package co.jinear.core.model.enumtype.workspace;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkspaceAccountRoleType {
    OWNER(0),
    ADMIN(1),
    MEMBER(2),
    GUEST(3);

    private final int value;
}
