package co.jinear.core.model.enumtype.team;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamStateType {

    ARCHIVED(-1),
    ACTIVE(0);

    private final int value;
}
