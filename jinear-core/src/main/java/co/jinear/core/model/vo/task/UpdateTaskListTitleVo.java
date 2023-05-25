package co.jinear.core.model.vo.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateTaskListTitleVo {

    private String taskListId;
    private String title;
}
