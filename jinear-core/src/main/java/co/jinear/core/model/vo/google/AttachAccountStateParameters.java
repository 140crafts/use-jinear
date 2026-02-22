package co.jinear.core.model.vo.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AttachAccountStateParameters {

    private String workspaceId;
    @JsonProperty("a")
    private Boolean appLogin;
    @JsonProperty("c")
    private String csrf;
}
