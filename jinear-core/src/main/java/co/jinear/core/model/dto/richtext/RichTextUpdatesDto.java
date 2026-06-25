package co.jinear.core.model.dto.richtext;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RichTextUpdatesDto {

    private long headSeq;
    @ToString.Exclude
    private List<String> updates;
}
