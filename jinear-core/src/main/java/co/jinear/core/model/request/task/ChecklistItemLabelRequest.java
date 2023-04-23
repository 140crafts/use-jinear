package co.jinear.core.model.request.task;

import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class ChecklistItemLabelRequest extends BaseRequest {

    private String label;
}
