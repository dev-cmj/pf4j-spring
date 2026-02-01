package com.project.core.exception;

public class ClientErrorException extends RuntimeException {
    public ClientErrorException(String message) { super(message); }
}