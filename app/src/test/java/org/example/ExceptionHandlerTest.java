package org.example;

import org.example.command.Command;
import org.example.command.ImmediatelyRetry;
import org.example.command.ImmediatelyRetryTwice;
import org.example.command.Logging;
import org.example.command.Move;
import org.example.command.Retry;
import org.example.exception.ReadLocationException;
import org.example.handler.ExceptionHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerTest {

    // п.5 SOLID и исключения
    @Test
    void createHandlerRetry_success() {
        ExceptionHandler.registerHandler("org.example.command.Move",
            "org.example.exception.ReadLocationException",
            (command, exception) -> new Retry(new Logging(command, exception)));

        var command = mock(Move.class);
        var exception = mock(ReadLocationException.class);
        var result = ExceptionHandler.create(command, exception);

        assertEquals(Retry.class, result.getClass());
    }

    // п.7 SOLID и исключения
    @Test
    void createHandlerImmediatelyRetry_success() {
        ExceptionHandler.registerHandler("org.example.command.Move",
            "org.example.exception.ReadLocationException",
            (command, exception) -> new ImmediatelyRetry(command));

        var command = mock(Move.class);
        var exception = mock(ReadLocationException.class);
        var result = ExceptionHandler.create(command, exception);

        assertEquals(ImmediatelyRetry.class, result.getClass());
    }

    // п.8 SOLID и исключения
    @Test
    void createHandlerImmediatelyRetryAndLog_success() {
        var countException = new AtomicReference<>(0);
        var sequenceCommands = new HashMap<Integer, BiFunction<Command, Exception, Command>>(2);
        sequenceCommands.put(1, (command, exception) -> new ImmediatelyRetry(command));
        sequenceCommands.put(2, Logging::new);

        ExceptionHandler.registerHandler("org.example.command.Move",
            "org.example.exception.ReadLocationException",
            (command, exception) -> {
                countException.set(countException.get() + 1);
                return sequenceCommands.get(countException.get())
                    .apply(command, exception);
            });

        var command = mock(Move.class);
        var exception = mock(ReadLocationException.class);
        var result = ExceptionHandler.create(command, exception);

        assertEquals(ImmediatelyRetry.class, result.getClass());

        result = ExceptionHandler.create(command, exception);

        assertEquals(Logging.class, result.getClass());
    }

    // п.9 SOLID и исключения
    @Test
    void createHandlerImmediatelyRetryTwiceAndLog_success() {
        var countException = new AtomicReference<>(0);
        var sequenceCommands = new HashMap<Integer, BiFunction<Command, Exception, Command>>(2);
        sequenceCommands.put(1, (command, exception) -> new ImmediatelyRetry(command));
        sequenceCommands.put(2, (command, exception) -> new ImmediatelyRetryTwice(command));
        sequenceCommands.put(3, Logging::new);

        ExceptionHandler.registerHandler("org.example.command.Move",
            "org.example.exception.ReadLocationException",
            (command, exception) -> {
                countException.set(countException.get() + 1);
                return sequenceCommands.get(countException.get())
                    .apply(command, exception);
            });

        var command = mock(Move.class);
        var exception = mock(ReadLocationException.class);
        var result = ExceptionHandler.create(command, exception);

        assertEquals(ImmediatelyRetry.class, result.getClass());

        result = ExceptionHandler.create(command, exception);

        assertEquals(ImmediatelyRetryTwice.class, result.getClass());

        result = ExceptionHandler.create(command, exception);

        assertEquals(Logging.class, result.getClass());
    }
}
