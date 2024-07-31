package co.jinear.core.model.enumtype.project;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectStateType {
    BACKLOG(1),
    PLANNED(2),
    IN_PROGRESS(3),
    COMPLETED(4),
    CANCELLED(5);

    private final int value;
}
