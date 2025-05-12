package co.jinear.core.model.dto.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskBoardEntryDetailedDto extends TaskBoardEntryDto {

    private TaskBoardDto taskBoard;
}
