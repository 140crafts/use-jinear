package co.jinear.core.config.interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

@Component
public class GenericResponseErrorHandler extends DefaultResponseErrorHandler {

    @Override
    protected boolean hasError(HttpStatusCode statusCode) {
        return statusCode.is5xxServerError() || (statusCode.is4xxClientError() && !HttpStatus.BAD_REQUEST.equals(statusCode));
    }
}
