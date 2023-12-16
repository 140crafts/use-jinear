package co.jinear.core.model.vo.task;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InitializeTaskFeedItemVo {

    private String taskId;
    private String feedId;
    private String feedItemId;
}
