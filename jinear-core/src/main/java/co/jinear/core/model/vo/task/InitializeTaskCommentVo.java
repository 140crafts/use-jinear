package co.jinear.core.model.vo.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InitializeTaskCommentVo {

    private String taskId;
    private String ownerId;
    private String comment;
    private String quoteCommentId;
}
