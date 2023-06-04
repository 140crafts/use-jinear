package co.jinear.core.model.enumtype.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskBoardStateType {

    OPEN(0),
    CLOSED(1);

    private int value;
}
