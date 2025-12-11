package com.atachakki.dto;

import java.time.LocalDateTime;

public class ErrorResponse<T> {

    private final boolean success;
    private final String message;
    private final T details;
    private final LocalDateTime timestamp;
    private final String path;

    public ErrorResponse(String message, T data, String path) {
        this.success = false;
        this.message = message;
        this.details = data;
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }

    public ErrorResponse(String message, String path) {
        this(message, null, path);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Object getDetails() {
        return details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }
}
