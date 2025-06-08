package co.jinear.core.model.request.project;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ProjectPostInitializeRequest extends BaseRequest {

    @NotBlank
    private String projectId;
    @NotBlank
    private String body;
    private String slug;
    @Nullable
    private List<MultipartFile> files;
}
