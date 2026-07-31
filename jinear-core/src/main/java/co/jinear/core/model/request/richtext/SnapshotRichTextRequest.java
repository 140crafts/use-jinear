package co.jinear.core.model.request.richtext;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class SnapshotRichTextRequest extends BaseRequest {

    /**
     * Base64-encoded merged full-state bytes that fold in every update up to {@link #upToSeq}.
     */
    @NotBlank
    @ToString.Exclude
    private String state;

    /**
     * The seq the client is compacting up to. Clamped to the current head server-side.
     */
    @NotNull
    private Long upToSeq;
}
