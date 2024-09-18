package co.jinear.core.model.enumtype.project;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectPriorityType {
    NONE(0),
    URGENT(1),
    HIGH(2),
    MEDIUM(3),
    LOW(4);

    private final int value;
}
