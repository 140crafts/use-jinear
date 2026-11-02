package co.jinear.core.model.dto.richtext;

import co.jinear.core.model.enumtype.richtext.RichTextFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RichTextDocumentDto {

    private RichTextFormat format;
    private long headSeq;
    @ToString.Exclude
    private List<String> states;
    @ToString.Exclude
    private String value;
}
