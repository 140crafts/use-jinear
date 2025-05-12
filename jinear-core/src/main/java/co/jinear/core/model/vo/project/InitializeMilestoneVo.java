package co.jinear.core.model.vo.project;

import co.jinear.core.model.enumtype.project.MilestoneStateType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class InitializeMilestoneVo {
    private String projectId;
    private String title;
    private String description;
    private ZonedDateTime targetDate;
    private MilestoneStateType milestoneState = MilestoneStateType.IN_PROGRESS;
}
