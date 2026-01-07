package co.jinear.core.model.enumtype.material;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MaterialAccessType {

    OWNER_ONLY(0),
    WORKSPACE_MEMBERS(1),
    GRAINED(2),
    ANYONE_WITH_LINK(3);

    private final int value;
}
