package co.jinear.core.controller.advice;

import co.jinear.core.config.locale.LocaleMessage;
import co.jinear.core.config.locale.MessageSourceLocalizer;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.exception.NotValidException;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.system.NormalizeHelper;
import jakarta.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GeneralApiAdvice {

    private final ApiAdviceHelper apiAdviceHelper;
    private final MessageSourceLocalizer messageSourceLocalizer;

    @ExceptionHandler(value = BusinessException.class)
    protected ResponseEntity<BaseResponse> handleBusinessException(BusinessException ex) {
        LocaleMessage localeMessage = messageSourceLocalizer.getLocaleMessage(ex.getMessage(), ex.getArguments());
        log.error(ex.getMessage(), ex);
        try {
            BaseResponse baseResponse = apiAdviceHelper.createErrorResponse(localeMessage);
            baseResponse.setAdditionalInfo(ex.getAdditionalInfo());
            return new ResponseEntity<>(baseResponse, HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return apiAdviceHelper.getUnknownExceptionResponse(ex);
        }
    }

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<BaseResponse> handleNotFound(NotFoundException ex) {
        LocaleMessage localeMessage = messageSourceLocalizer.getLocaleMessage(ex.getMessage(), ex.getArguments());
        log.error(ex.getMessage(), ex);
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
        log.error(ex.getMessage(), ex);
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
        log.error(ex.getMessage(), ex);
        try {
            BaseResponse baseResponse = apiAdviceHelper.createErrorResponse(localeMessage);
            return new ResponseEntity<>(baseResponse, HttpStatus.PRECONDITION_FAILED);
        } catch (Exception e) {
            return apiAdviceHelper.getUnknownExceptionResponse(ex);
        }
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        try {
            List<LocaleMessage> messages = ex.getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(err -> err.unwrap(ConstraintViolation.class))
                    .map(this::generateConstraintViolationMessage)
                    .toList();

            StringBuilder sb = new StringBuilder();
            messages.stream()
                    .map(LocaleMessage::getConsumerErrorMessage)
                    .forEach(consumerMessage -> sb.append(consumerMessage).append(NormalizeHelper.SPACE_STRING));

            log.error(ex.getMessage(), ex);
            BaseResponse baseResponse = apiAdviceHelper.createResponse("", sb.toString(), sb.toString());
            return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return apiAdviceHelper.getUnknownExceptionResponse(ex);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleUnknownException(Exception ex) {
        return apiAdviceHelper.getUnknownExceptionResponse(ex);
    }

    private LocaleMessage generateConstraintViolationMessage(ConstraintViolation err) {
        return messageSourceLocalizer.getLocaleMessage(err.getMessage(), err.getPropertyPath());
    }
}
