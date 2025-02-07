package com.chuckboliver.bookservice.common;

import java.util.List;

public record ValidationErrorResponse(List<ErrorDetail> fieldErrors) {

    public record ErrorDetail(String field, Object rejectedValue, String message) {
    }
}
