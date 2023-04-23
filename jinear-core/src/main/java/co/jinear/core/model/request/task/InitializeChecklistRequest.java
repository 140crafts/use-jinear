package co.jinear.core.model.request.task;

import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class InitializeChecklistRequest extends BaseRequest {

    private String title;
    private String initialItemLabel;
}
