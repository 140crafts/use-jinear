package co.jinear.core.exception;

import lombok.Getter;

@Getter
public class NoAccessException extends RuntimeException {

    private final Object[] arguments;

    public NoAccessException() {
        super("common.error.no-access");
        this.arguments = new Object[0];
    }

    public NoAccessException(String message, Object... arguments) {
        super(message);
        this.arguments = arguments;
    }
}
