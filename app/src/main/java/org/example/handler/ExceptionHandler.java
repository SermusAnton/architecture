package org.example.handler;

import org.example.command.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ExceptionHandler {

    private static final Map<CommandRuntimeExceptionKey, BiFunction<Command, Exception, Command>> store = new HashMap<>();
    private static BiFunction<Command, Exception, Command> defaultCommand;

    public static Command create(Command command, Exception exception) {
        var classCommand = command.getClass().getName();
        var classException = exception.getClass().getName();
        var handler = store.getOrDefault(new CommandRuntimeExceptionKey(classCommand, classException), defaultCommand);
        return handler.apply(command, exception);
    }

    public static void registerHandler(String classCommand, String classException, BiFunction<Command, Exception, Command> handler) {
        store.put(new CommandRuntimeExceptionKey(classCommand, classException), handler);
    }
}
