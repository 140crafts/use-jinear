package co.jinear.core.model.vo.project;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class UpdateProjectDatesVo {
    private ZonedDateTime startDate;
    private Boolean updateStartDate;
    private ZonedDateTime targetDate;
    private Boolean updateTargetDate;
}
