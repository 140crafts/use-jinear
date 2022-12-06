package co.jinear.core.model.request.task;

import co.jinear.core.model.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskRetrieveIntersectingRequest extends BaseRequest {
    private String workspaceId;
    private String teamId;
    private ZonedDateTime timespanStart;
    private ZonedDateTime timespanEnd;
}
