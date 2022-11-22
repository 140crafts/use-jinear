package co.jinear.core.exception.auth;

import co.jinear.core.exception.BusinessException;

public class NotSupportedAuthStrategyException extends BusinessException {

    public NotSupportedAuthStrategyException() {
        super("common.error.system.unknown");
    }
}
