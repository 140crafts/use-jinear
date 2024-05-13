package co.jinear.core.service.client.messageapi.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EmitRequest {

    private String channel;
    private String topic;
    private String message;
}
