package co.jinear.core.exception.note;

import co.jinear.core.exception.BusinessException;

public class RichTextHeadMovedException extends BusinessException {

    public RichTextHeadMovedException() {
        super("note.error.edit.head-moved");
    }
}
