package org.example.handler;

import java.util.Objects;

public class CommandRuntimeExceptionKey {
    private final String classCommand;

    private final String classException;

    public CommandRuntimeExceptionKey(String classCommand, String classException) {
        this.classCommand = classCommand;
        this.classException = classException;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommandRuntimeExceptionKey that = (CommandRuntimeExceptionKey) o;
        return Objects.equals(classCommand, that.classCommand) && Objects.equals(classException, that.classException);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classCommand, classException);
    }
}
