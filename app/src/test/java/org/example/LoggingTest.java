package org.example;

import org.example.command.Command;
import org.example.command.Logging;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoggingTest {

    @Mock
    RuntimeException exception;

    @Mock
    Command command;

    @Mock
    Logger logger;

    @InjectMocks
    Logging logging;

    // п.4 SOLID и исключения
    @Test
    void logException_success() {
        var exitingExceptionMessage = "exceptionMessage";
        when(exception.getMessage()).thenReturn(exitingExceptionMessage);

        logging.execute();

        verify(logger, times(1)).error("Command {} throw {}", command.getClass().getName(), exception.getMessage());
    }
}
