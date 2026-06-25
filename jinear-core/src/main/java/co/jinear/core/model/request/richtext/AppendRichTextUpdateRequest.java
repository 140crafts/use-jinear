package co.jinear.core.model.request.richtext;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class AppendRichTextUpdateRequest extends BaseRequest {

    @NotBlank
    @ToString.Exclude
    private String update;

    @Nullable
    @ToString.Exclude
    private String html;
}
