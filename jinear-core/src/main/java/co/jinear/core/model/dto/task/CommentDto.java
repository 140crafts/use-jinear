package co.jinear.core.model.dto.task;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto extends BaseDto {

    private String commentId;
    private String taskId;
    private String ownerId;
    private String richTextId;
    private PlainAccountProfileDto owner;
    private RichTextDto richText;
    private CommentDto quote;
}
