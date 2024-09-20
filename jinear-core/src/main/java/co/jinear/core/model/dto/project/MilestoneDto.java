package co.jinear.core.model.dto.project;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.enumtype.project.MilestoneStateType;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class MilestoneDto extends BaseDto {

    private String milestoneId;
    private String projectId;
    private String title;
    private String descriptionRichTextId;
    private Integer milestoneOrder;
    private ZonedDateTime targetDate;
    private RichTextDto description;
    private MilestoneStateType milestoneState;
}
