package co.jinear.core.model.dto.task;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChecklistItemDto extends BaseDto {

    private String checklistItemId;
    private String checklistId;
    private String label;
    private Boolean isChecked;
}
