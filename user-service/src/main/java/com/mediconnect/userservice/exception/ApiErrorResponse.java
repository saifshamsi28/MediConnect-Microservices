package com.mediconnect.userservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ApiErrorResponse {
    private LocalDateTime timeStamp;
    private HttpStatus statusCode;
    private String error;
    private String message;
    private String path;
}
