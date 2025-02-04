package com.chuckboliver.bookservice.common;

import lombok.Value;

import java.util.List;

@Value
public class ValidationErrorResponse {

    List<ErrorDetail> fieldErrors;

    @Value
    public static class ErrorDetail {
        String field;
        Object rejectedValue;
        String message;
    }
}
