package com.project.core.controller;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@ControllerAdvice
public class GatewayExceptionHandler {

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<String> handleResponseException(RestClientResponseException e) {
        return ResponseEntity.status(e.getStatusCode())
                .headers(e.getResponseHeaders())
                .body(e.getResponseBodyAsString());
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<String> handleClientException(RestClientException e) {
        return ResponseEntity.status(502).body("Backend unavailable: " + e.getMessage());
    }
}

