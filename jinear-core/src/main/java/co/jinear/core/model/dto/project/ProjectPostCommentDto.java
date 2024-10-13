package co.jinear.core.model.dto.project;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectPostCommentDto extends BaseDto {

    private String projectPostCommentId;
    private String projectPostId;
    private String accountId;
    private String commentBodyRichTextId;
    private PlainAccountProfileDto account;
    private RichTextDto commentBody;
    private ProjectPostCommentDto quote;
}
