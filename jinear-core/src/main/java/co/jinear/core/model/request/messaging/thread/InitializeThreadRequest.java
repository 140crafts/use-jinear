package co.jinear.core.model.request.messaging.thread;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InitializeThreadRequest extends BaseRequest {

    @NotBlank
    private String channelId;
    @NotBlank
    private String initialMessageBody;
}
