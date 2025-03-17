package org.example;

import org.example.command.Move;
import org.example.common.polling.DefaultState;
import org.example.common.polling.Receiver;
import org.example.common.polling.ServerThreadWithState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultStateTest {

    @Mock
    ServerThreadWithState serverThreadWithState;
    @Mock
    Receiver receiver;
    @Mock
    Move move;

    @InjectMocks
    DefaultState defaultState;

    @Test
    void run_InterruptedException_interrupt() throws InterruptedException {
        when(receiver.get())
            .thenThrow(new InterruptedException());
        when(serverThreadWithState.getReceiver())
            .thenReturn(receiver);

        assertDoesNotThrow(() -> defaultState.run());
    }

    @Test
    void run_RuntimeException_interrupt() throws InterruptedException {
        when(receiver.get())
            .thenReturn(move);
        when(serverThreadWithState.getReceiver())
            .thenReturn(receiver);
        doThrow(new RuntimeException()).when(move).execute();

        assertDoesNotThrow(() -> defaultState.run());
    }
}
