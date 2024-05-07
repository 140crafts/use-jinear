package co.jinear.core.model.request.messaging.message;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

@Getter
@Setter
public class SendMessageRequest extends BaseRequest {

    @NotNull
    private String body;
    @Nullable
    private Map<String, String> data;
}
