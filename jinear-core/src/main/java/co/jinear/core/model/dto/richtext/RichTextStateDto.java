package co.jinear.core.model.dto.richtext;

import co.jinear.core.model.enumtype.richtext.RichTextFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RichTextStateDto {

    private RichTextFormat format;
    @ToString.Exclude
    private String yjsState;
    private long yjsStateSeq;
    private long headSeq;
}
