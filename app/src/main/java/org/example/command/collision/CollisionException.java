package org.example.command.collision;

public class CollisionException extends RuntimeException {
    public CollisionException(Exception exception) {
        super(exception);
    }
}