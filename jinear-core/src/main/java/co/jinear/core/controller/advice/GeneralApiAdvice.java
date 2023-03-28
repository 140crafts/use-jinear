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
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
                    .map(messageSourceLocalizer::getLocaleMessage)
                    .toList();

            Set<String> errorMessages = messages.stream().map(apiAdviceHelper::getErrorMessage).collect(Collectors.toSet());
            String unifiedErrorMessage = StringUtils.join(errorMessages, NormalizeHelper.COMMA_SEPARATOR);
            log.error(ex.getMessage(), ex);
            BaseResponse baseResponse = apiAdviceHelper.createResponse("", unifiedErrorMessage, unifiedErrorMessage);
            return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return apiAdviceHelper.getUnknownExceptionResponse(ex);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleUnknownException(Exception ex) {
        return apiAdviceHelper.getUnknownExceptionResponse(ex);
    }

    private String generateConstraintViolationMessage(ConstraintViolation err) {
        StringBuilder sb = new StringBuilder();
        Optional.ofNullable(err).map(ConstraintViolation::getPropertyPath).map(propertyPath -> sb.append(propertyPath).append(NormalizeHelper.SPACE_STRING));
        Optional.ofNullable(err).map(ConstraintViolation::getMessage).map(sb::append);
        return sb.toString();
    }
}
