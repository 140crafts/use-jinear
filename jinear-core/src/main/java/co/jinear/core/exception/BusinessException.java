package co.jinear.core.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class BusinessException extends RuntimeException {

    private final Object[] arguments;
    private final Map<String, String> additionalInfo;

    public BusinessException() {
        super("common.error.system.unknown");
        this.arguments = new Object[0];
        this.additionalInfo = null;
    }

    public BusinessException(String message) {
        super(message);
        this.arguments = new Object[0];
        this.additionalInfo = null;
    }

    public BusinessException(String message, Object... arguments) {
        super(message);
        this.arguments = arguments;
        this.additionalInfo = null;
    }

    public BusinessException(String message, Map<String, String> additionalInfo) {
        super(message);
        this.arguments = new Object[0];
        this.additionalInfo = additionalInfo;
    }

    public BusinessException(String message, Map<String, String> additionalInfo, Object... arguments) {
        super(message);
        this.arguments = arguments;
        this.additionalInfo = additionalInfo;
    }
}
