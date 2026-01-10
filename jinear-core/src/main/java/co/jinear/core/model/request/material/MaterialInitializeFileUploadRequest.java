package co.jinear.core.model.request.material;

import co.jinear.core.model.enumtype.material.MaterialAccessType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class MaterialInitializeFileUploadRequest extends BaseRequest {

    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    @NotBlank
    private String name;
    @Nullable
    private String icon;
    @Nullable
    private String color;
    @Nullable
    private String parentMaterialId;
    @Nullable
    private String contentType;
    @NotNull
    private Long fileSize;
    @Nullable
    private MaterialAccessType materialAccessType = MaterialAccessType.OWNER_ONLY;

    public String getContentType() {
        return (contentType == null || contentType.isBlank())
                ? DEFAULT_CONTENT_TYPE
                : contentType;
    }
}
