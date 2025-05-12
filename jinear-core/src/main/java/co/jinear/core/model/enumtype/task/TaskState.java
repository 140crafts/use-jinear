package co.jinear.core.model.enumtype.task;

import lombok.Getter;

@Getter
public enum TaskState {
    TO_DO,
    IN_PROGRESS,
    IN_TEST,
    WONT_DO,
    DONE;
}
