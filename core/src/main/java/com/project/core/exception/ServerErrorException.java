package com.project.core.exception;

public class ServerErrorException extends RuntimeException {
    public ServerErrorException(String message) { super(message); }
}