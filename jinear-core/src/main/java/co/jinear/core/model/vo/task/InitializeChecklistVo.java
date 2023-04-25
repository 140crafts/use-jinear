package co.jinear.core.model.vo.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InitializeChecklistVo {

    private String taskId;
    private String ownerId;
    private String title;
    private String initialItemLabel;
}
