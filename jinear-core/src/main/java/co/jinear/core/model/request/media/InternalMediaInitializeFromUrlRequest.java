package co.jinear.core.model.request.media;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class InternalMediaInitializeFromUrlRequest extends InternalMediaInitializeRequest {
    private String url;
}
