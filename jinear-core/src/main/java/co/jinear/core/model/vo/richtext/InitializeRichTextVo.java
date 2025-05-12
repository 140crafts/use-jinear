package co.jinear.core.model.vo.richtext;

import co.jinear.core.model.enumtype.richtext.RichTextType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InitializeRichTextVo {
    private String relatedObjectId;
    @ToString.Exclude
    private String value;
    private RichTextType type;
}
