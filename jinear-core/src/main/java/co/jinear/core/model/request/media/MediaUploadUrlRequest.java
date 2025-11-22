package co.jinear.core.model.request.media;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MediaUploadUrlRequest extends BaseRequest {

    @NotBlank
    private String originalName;
    @NotNull
    private Long fileSize;
    @NotBlank
    private String contentType;
}
