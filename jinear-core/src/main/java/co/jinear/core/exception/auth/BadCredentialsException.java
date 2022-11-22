package co.jinear.core.exception.auth;

import co.jinear.core.exception.BusinessException;
import lombok.Getter;

@Getter
public class BadCredentialsException extends BusinessException {

    private final Object[] arguments;

    public BadCredentialsException() {
        super("common.error.bad-credentials");
        this.arguments = new Object[0];
    }
}
