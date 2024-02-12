package co.jinear.core.config.interceptor;

import co.jinear.core.exception.client.google.UnauthorizedTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class GoogleClientsResponseErrorHandler extends DefaultResponseErrorHandler {

    @Override
    protected boolean hasError(HttpStatusCode statusCode) {
        if (UNAUTHORIZED.value() == statusCode.value()) {
            throw new UnauthorizedTokenException();
        }
        return statusCode.is5xxServerError() || (statusCode.is4xxClientError() && !HttpStatus.BAD_REQUEST.equals(statusCode));
    }

}
