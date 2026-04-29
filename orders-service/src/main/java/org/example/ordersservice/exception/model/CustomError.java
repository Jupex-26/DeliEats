package org.example.ordersservice.exception.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Getter
public class CustomError {
    private String message;
    private LocalDateTime timestamp;
    private int httpCode;
    private Map<String, String> fieldErrors;
}
