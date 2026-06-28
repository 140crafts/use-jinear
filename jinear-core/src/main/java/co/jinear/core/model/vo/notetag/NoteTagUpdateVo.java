package co.jinear.core.model.vo.notetag;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoteTagUpdateVo {

    private String noteTagId;
    private String name;
    private String color;
}
