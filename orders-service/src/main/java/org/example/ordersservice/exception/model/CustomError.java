package org.example.ordersservice.exception.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CustomError {
    private String message;
    private LocalDateTime timestamp;
    private int httpCode;
}
