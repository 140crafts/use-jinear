package co.jinear.core.model.vo.calendar;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InitializeCalendarVo {

    private String workspaceId;
    private String initializedBy;
    private String integrationInfoId;
    private String name;
}
