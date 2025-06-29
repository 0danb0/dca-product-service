package com.danb.dca.product_serivce.exceptions;
import com.danb.dca.product_serivce.controllers.InvoicesController;
import com.danb.dca.product_serivce.models.response.CommonErrorResponse;
import com.danb.dca.product_serivce.models.response.Error;
import com.danb.dca.product_serivce.enums.ConstantEnum;
import com.danb.dca.product_serivce.enums.DomainMsg;
import com.danb.dca.product_serivce.enums.ErrorMsg;
import com.danb.dca.product_serivce.utils.Tools;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Setter
@RestControllerAdvice(assignableTypes = {InvoicesController.class})
public class ErrorHandler implements RequestBodyAdvice {

    private static final ThreadLocal<ErrorHandler> requestInfoThreadLocal = new ThreadLocal<>();
    private String body;

    @Autowired
    Tools tools;

    public static ErrorHandler get() {
        ErrorHandler errorHandler = requestInfoThreadLocal.get();
        if (errorHandler == null) {
            errorHandler = new ErrorHandler();
            requestInfoThreadLocal.set(errorHandler);
        }
        return errorHandler;
    }

    private static void setBodyInThreadLocal(String body) {
        ErrorHandler errorHandler = get();
        errorHandler.setBody(body);
        setErrorHandler(errorHandler);
    }

    private static void setErrorHandler(ErrorHandler errorHandler) {
        requestInfoThreadLocal.set(errorHandler);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final CommonErrorResponse handleAllExceptions(Exception ex, HttpServletRequest request) {
        log.error("Exception: ", ex);
        return buildError(request, ErrorMsg.DCA_PRD_SRV_99.getCode(), ErrorMsg.DCA_PRD_SRV_99.getMessage(), ex.getMessage(), DomainMsg.MICROSERVICE_FUNCTIONAL.getName());
    }

    @ExceptionHandler(InvoiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final CommonErrorResponse handleInvoiceExceptions(InvoiceException ex, HttpServletRequest request) {
        log.error("Exception: ", ex);
        return buildError(request, ex.getCode(), ex.getMessage(), ex.getMessage(), DomainMsg.PRODUCT_SERVICE_TECHNICAL.getName());
    }

    @ExceptionHandler(MainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final CommonErrorResponse handleMainExceptions(InvoiceException ex, HttpServletRequest request, String code, String message) {
        log.error("Exception: ", ex);
        return buildError(request, code, message, ex.getMessage(), DomainMsg.PRODUCT_SERVICE_TECHNICAL.getName());
    }

    @ExceptionHandler({ RuntimeException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final CommonErrorResponse handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        log.error("Exception: ", ex);
        return buildError(request, ErrorMsg.DCA_PRD_SRV_99.getCode(), ErrorMsg.DCA_PRD_SRV_99.getMessage(), ex.getMessage(), DomainMsg.MICROSERVICE_FUNCTIONAL.getName());
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class, MissingServletRequestParameterException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final CommonErrorResponse handleValidationException(Exception ex, HttpServletRequest request) {

        log.error("MethodArgumentNotValidException: ", ex);

        Map<String, String> fieldsMap = new HashMap<>();

        if (ex instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            BindingResult result = methodArgumentNotValidException.getBindingResult();

            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldsMap = fieldErrors.stream().collect(Collectors.groupingBy(FieldError::getField, Collectors.mapping(FieldError::getDefaultMessage, Collectors.joining(","))));

        } else if (ex instanceof MissingServletRequestParameterException missingServletRequestParameterException) {
            fieldsMap.put(missingServletRequestParameterException.getParameterName(), missingServletRequestParameterException.getMessage());
        } else {
            return buildError(request, ErrorMsg.DCA_PRD_SRV_01.getCode(), ErrorMsg.DCA_PRD_SRV_01.getMessage(), ex.getMessage(), DomainMsg.MICROSERVICE_FUNCTIONAL.getName());
        }

        return buildInvalidFieldsError(request, fieldsMap);
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final CommonErrorResponse handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        log.error("ConstraintViolationException: ", ex);

        Map<String, String> fieldsMap = new HashMap<>();
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        for (ConstraintViolation<?> violation : constraintViolations) {
            String fieldName = null;

            for (Path.Node node : violation.getPropertyPath()) {
                fieldName = node.getName();
            }

            String messages = violation.getMessage();

            if (fieldsMap.containsKey(fieldName)) {
                String message = fieldsMap.get(fieldName);
                messages += "," + message;
                fieldsMap.put(fieldName, messages);
            } else {
                fieldsMap.put(fieldName, messages);
            }
        }

        return buildInvalidFieldsError(request, fieldsMap);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final CommonErrorResponse handleInvalidJson(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.error("HttpMessageNotReadableException: ", ex);
        return buildError(request,
                ErrorMsg.DCA_PRD_SRV_01.getCode(),
                "Malformed JSON request",
                ex.getMessage(),
                DomainMsg.MICROSERVICE_FUNCTIONAL.getName());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public final CommonErrorResponse handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        log.error("HttpRequestMethodNotSupportedException: ", ex);
        return buildError(request,
                ErrorMsg.DCA_PRD_SRV_01.getCode(),
                "HTTP method not allowed",
                ex.getMessage(),
                DomainMsg.MICROSERVICE_FUNCTIONAL.getName());
    }

    private CommonErrorResponse buildInvalidFieldsError(HttpServletRequest request, Map<String, String> fieldsMap) {
        String message = "Invalid Fields: " + StringUtils.join(fieldsMap.keySet(), ',');

        return buildError(request, ErrorMsg.DCA_PRD_SRV_01.getCode(), message, StringUtils.join(fieldsMap.values(), ','), DomainMsg.MICROSERVICE_FUNCTIONAL.getName());
    }

    private CommonErrorResponse buildError(HttpServletRequest request, String code, String message, String detailedMessage, String domain) {
        com.danb.dca.product_serivce.models.response.Error error = new Error();
        error.setCode(code);
        error.setMessage(message);
        error.setDetailed(detailedMessage);
        error.setDomain(domain);

        try {
            ErrorHandler handler = requestInfoThreadLocal.get();
            if (handler != null && handler.body != null) {
                log.error("Error: {}, message: {}, detailedMessage: {}, domainMsg: {}, request: {}", code, message, detailedMessage, domain, handler.body);
            }
        } finally {
            requestInfoThreadLocal.remove();
        }

        CommonErrorResponse commonErrorResponse = new CommonErrorResponse();
        commonErrorResponse.setError(error);
        commonErrorResponse.setTimestamp(tools.getInstant());
        commonErrorResponse.setDomain(ConstantEnum.APPLICATION_NAME.getValue());
        return commonErrorResponse;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType)
            throws IOException {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        ErrorHandler.setBodyInThreadLocal(body.toString());
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

}

