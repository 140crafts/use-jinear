package co.jinear.core.exception;

import lombok.Getter;

@Getter
public class NotValidException extends RuntimeException {

    private final Object[] arguments;

    public NotValidException() {
        super("common.error.not-valid");
        this.arguments = new Object[0];
    }

    public NotValidException(String message, Object... arguments) {
        super(message);
        this.arguments = arguments;
    }
}
