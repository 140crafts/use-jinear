package co.jinear.core.model.dto.task;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ChecklistDto extends BaseDto {

    private String checklistId;
    private String taskId;
    private String ownerId;
    private String title;
    private Set<ChecklistItemDto> checklistItems;
}
