package com.example.monitor.api;

public class StatusWithDescription {

    private RequestStatus status;
    private String message;

    public StatusWithDescription(RequestStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
