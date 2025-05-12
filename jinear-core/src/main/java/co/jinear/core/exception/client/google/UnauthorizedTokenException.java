package co.jinear.core.exception.client.google;

import co.jinear.core.exception.BusinessException;

public class UnauthorizedTokenException extends BusinessException {

    public UnauthorizedTokenException() {
        super("google.token-expired");
    }
}
