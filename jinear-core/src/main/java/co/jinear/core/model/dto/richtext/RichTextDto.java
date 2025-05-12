package co.jinear.core.model.dto.richtext;

import co.jinear.core.model.enumtype.richtext.RichTextSourceStack;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RichTextDto {

    private String richTextId;
    private String relatedObjectId;
    private String value;
    private RichTextType type;
    private RichTextSourceStack sourceStack;
}
