package co.jinear.core.exception.note;

import co.jinear.core.exception.BusinessException;

public class NoteEditRejectedException extends BusinessException {

    public NoteEditRejectedException(String message, Object... arguments) {
        super(message, arguments);
    }
}
