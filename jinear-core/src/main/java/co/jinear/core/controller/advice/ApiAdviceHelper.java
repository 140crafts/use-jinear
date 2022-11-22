package co.jinear.core.controller.advice;

import co.jinear.core.config.locale.LocaleMessage;
import co.jinear.core.config.locale.MessageSourceLocalizer;
import co.jinear.core.model.enumtype.ResponseStatusType;
import co.jinear.core.model.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiAdviceHelper {

    private static final String ERROR_MESSAGE_SPLITTER = ";";
    private final MessageSourceLocalizer messageSourceLocalizer;

    public BaseResponse createErrorResponse(LocaleMessage localeMessage) {
        String[] errorCodeMessage = localeMessage.getErrorMessage().split(ERROR_MESSAGE_SPLITTER);
        return createResponse(errorCodeMessage[0], errorCodeMessage[1], getConsumerErrorMessage(localeMessage.getConsumerErrorMessage()));
    }

    public ResponseEntity<BaseResponse> getUnknownExceptionResponse(Exception ex) {
        String genericErrorCode = "common.error.system.unknown";
        LocaleMessage localeMessage = messageSourceLocalizer.getLocaleMessage(genericErrorCode);
        log.error("Unknown exception", ex);
        BaseResponse baseResponse = createErrorResponse(localeMessage);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
    }

    private String getConsumerErrorMessage(String consumerErrorMessage) {
        String[] errorCodeMessage = consumerErrorMessage.split(ERROR_MESSAGE_SPLITTER);
        return errorCodeMessage[1] + " (" + errorCodeMessage[0] + ")";
    }

    private BaseResponse createResponse(String errorCode, String errorMessage, String consumerErrorMessage) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResponseStatusType(ResponseStatusType.FAILURE);
        baseResponse.setErrorCode(errorCode);
        baseResponse.setErrorMessage(errorMessage);
        baseResponse.setConsumerErrorMessage(consumerErrorMessage);
        baseResponse.setSystemTime(Clock.systemUTC().millis());
        return baseResponse;
    }
}
