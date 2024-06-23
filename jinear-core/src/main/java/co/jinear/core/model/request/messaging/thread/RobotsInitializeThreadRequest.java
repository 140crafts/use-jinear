package co.jinear.core.model.request.messaging.thread;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RobotsInitializeThreadRequest extends BaseRequest {

    @NotBlank
    private String initialMessageBody;
}
