package co.jinear.core.model.dto.task;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class PlainTaskBoardEntryDto extends BaseDto {

    private String taskBoardEntryId;
    private String taskBoardId;
    private String taskId;
    private Integer order;
    private TaskBoardDto taskBoard;
}
