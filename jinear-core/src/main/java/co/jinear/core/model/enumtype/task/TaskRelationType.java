package co.jinear.core.model.enumtype.task;

import lombok.Getter;

@Getter
public enum TaskRelationType {
    BLOCKS,
    IS_BLOCKED_BY,
    SUBTASK
}
