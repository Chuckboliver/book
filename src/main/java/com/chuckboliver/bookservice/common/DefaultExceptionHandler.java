package com.chuckboliver.bookservice.common;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        List<ValidationErrorResponse.ErrorDetail> errorDetails = ex.getConstraintViolations().stream()
                .map(violation -> new ValidationErrorResponse.ErrorDetail(
                        violation.getPropertyPath().toString(),
                        violation.getInvalidValue(),
                        violation.getMessage())
                )
                .toList();

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(errorDetails);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ValidationErrorResponse.ErrorDetail> errorDetails = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ValidationErrorResponse.ErrorDetail(
                        fieldError.getField(),
                        fieldError.getRejectedValue(),
                        fieldError.getDefaultMessage()
                ))
                .toList();

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(errorDetails);

        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }
}
