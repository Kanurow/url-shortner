package com.rowland.engineering.shortner.exception;

public class PageLimitExceededException extends RuntimeException{
    public PageLimitExceededException(String message) {
        super(message);
    }
}
