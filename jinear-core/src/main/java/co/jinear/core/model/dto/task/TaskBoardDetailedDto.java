package co.jinear.core.model.dto.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskBoardDetailedDto extends TaskBoardDto {

    private Set<TaskBoardEntryDto> taskListEntries;
}
