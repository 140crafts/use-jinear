package co.jinear.core.model.dto.project;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.media.AccessibleMediaDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ProjectPostDto extends BaseDto {

    private String projectPostId;
    private String projectId;
    private String accountId;
    private String feedAccessKey;
    private PlainAccountProfileDto account;
    private RichTextDto postBody;
    private Set<AccessibleMediaDto> files;
    private Long commentCount;
    private String slug;
}
