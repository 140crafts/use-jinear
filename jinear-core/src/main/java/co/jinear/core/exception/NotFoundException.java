package co.jinear.core.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private final Object[] arguments;

    public NotFoundException() {
        super("common.error.not-found");
        this.arguments = new Object[0];
    }

    public NotFoundException(String message, Object... arguments) {
        super(message);
        this.arguments = arguments;
    }
}
