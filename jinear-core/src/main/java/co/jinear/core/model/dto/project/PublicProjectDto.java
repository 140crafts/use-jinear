package co.jinear.core.model.dto.project;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class PublicProjectDto extends BaseDto {

    private String projectId;
    private String workspaceId;
    private String title;
    private Boolean archived;
    @Nullable
    private RichTextDto info;
}
