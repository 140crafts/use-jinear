package co.jinear.core.model.enumtype.team;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamWorkflowStateGroup {
    BACKLOG(1),
    NOT_STARTED(2),
    STARTED(3),
    COMPLETED(4),
    CANCELLED(5);

    private final int value;
}
