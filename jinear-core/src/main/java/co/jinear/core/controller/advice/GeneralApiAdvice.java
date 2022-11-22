package co.jinear.core.controller.advice;

import co.jinear.core.config.locale.LocaleMessage;
import co.jinear.core.config.locale.MessageSourceLocalizer;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.exception.NotValidException;
import co.jinear.core.model.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class GeneralApiAdvice extends ResponseEntityExceptionHandler {

    private final ApiAdviceHelper apiAdviceHelper;
    private final MessageSourceLocalizer messageSourceLocalizer;

    @ExceptionHandler(value = BusinessException.class)
    protected ResponseEntity<BaseResponse> handleBusinessException(BusinessException ex) {
        LocaleMessage localeMessage = messageSourceLocalizer.getLocaleMessage(ex.getMessage(), ex.getArguments());
        logger.error(ex.getMessage(), ex);
        try {
            BaseResponse baseResponse = apiAdviceHelper.createErrorResponse(localeMessage);
            return new ResponseEntity<>(baseResponse, HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return apiAdviceHelper.getUnknownExceptionResponse(ex);
        }
    }

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<BaseResponse> handleNotFound(NotFoundException ex) {
        LocaleMessage localeMessage = messageSourceLocalizer.getLocaleMessage(ex.getMessage(), ex.getArguments());
        logger.error(ex.getMessage(), ex);
        try {
            BaseResponse baseResponse = apiAdviceHelper.createErrorResponse(localeMessage);
            return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return apiAdviceHelper.getUnknownExceptionResponse(ex);
        }
    }

    @ExceptionHandler(value = NoAccessException.class)
    protected ResponseEntity<BaseResponse> handleNoAccess(NoAccessException ex) {
        LocaleMessage localeMessage = messageSourceLocalizer.getLocaleMessage(ex.getMessage(), ex.getArguments());
        logger.error(ex.getMessage(), ex);
        try {
            BaseResponse baseResponse = apiAdviceHelper.createErrorResponse(localeMessage);
            return new ResponseEntity<>(baseResponse, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return apiAdviceHelper.getUnknownExceptionResponse(ex);
        }
    }

    @ExceptionHandler(value = NotValidException.class)
    protected ResponseEntity<BaseResponse> handleNotValid(NotValidException ex) {
        LocaleMessage localeMessage = messageSourceLocalizer.getLocaleMessage(ex.getMessage(), ex.getArguments());
        logger.error(ex.getMessage(), ex);
        try {
            BaseResponse baseResponse = apiAdviceHelper.createErrorResponse(localeMessage);
            return new ResponseEntity<>(baseResponse, HttpStatus.PRECONDITION_FAILED);
        } catch (Exception e) {
            return apiAdviceHelper.getUnknownExceptionResponse(ex);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleUnknownException(Exception ex) {
        return apiAdviceHelper.getUnknownExceptionResponse(ex);
    }
}
