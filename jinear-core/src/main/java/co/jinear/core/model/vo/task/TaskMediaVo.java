package co.jinear.core.model.vo.task;

import co.jinear.core.model.entity.media.Media;
import co.jinear.core.model.entity.task.Task;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TaskMediaVo {

    private Task task;
    private Media media;

    public TaskMediaVo(Task task, Media media) {
        this.task = task;
        this.media = media;
    }
}
