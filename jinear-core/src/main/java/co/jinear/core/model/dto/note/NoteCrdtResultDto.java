package co.jinear.core.model.dto.note;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoteCrdtResultDto {

    @ToString.Exclude
    private String update;
    @ToString.Exclude
    private String html;
    private String title;
    private boolean changed;
}
