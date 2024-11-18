package org.example.exception;

public class CommandException extends RuntimeException {
    public CommandException(Exception exception) {
        super(exception);
    }
}
