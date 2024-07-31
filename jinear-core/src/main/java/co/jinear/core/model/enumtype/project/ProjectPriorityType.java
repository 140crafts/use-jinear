package co.jinear.core.model.enumtype.project;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectPriorityType {
    URGENT(1),
    HIGH(2),
    MEDIUM(3),
    LOW(4);

    private final int value;
}
