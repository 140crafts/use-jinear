package co.jinear.core.model.enumtype.project;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MilestoneStateType {
    IN_PROGRESS(0),
    COMPLETED(1);

    private final int value;
}
