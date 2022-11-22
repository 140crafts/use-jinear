package co.jinear.core.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final Object[] arguments;

    public BusinessException() {
        super("common.error.system.unknown");
        this.arguments = new Object[0];
    }

    public BusinessException(String message) {
        super(message);
        this.arguments = new Object[0];
    }

    public BusinessException(String message, Object... arguments) {
        super(message);
        this.arguments = arguments;
    }
}
