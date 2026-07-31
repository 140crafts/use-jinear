package co.jinear.core.model.request.richtext;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class SeedRichTextRequest extends BaseRequest {

    @NotBlank
    @ToString.Exclude
    private String state;
}
