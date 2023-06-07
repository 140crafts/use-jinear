package co.jinear.core.model.dto.task;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.PageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskAndTaskBoardRelationDto extends BaseDto {

    private List<TaskBoardEntryDetailedDto> alreadyAddedBoards;
    private PageDto<TaskBoardDto> recentBoards;
}
