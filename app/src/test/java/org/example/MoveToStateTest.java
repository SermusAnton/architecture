package org.example;

import org.example.command.Command;
import org.example.command.Move;
import org.example.common.polling.MoveToState;
import org.example.common.polling.Receiver;
import org.example.common.polling.ServerThreadWithState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MoveToStateTest {

    @Mock
    ServerThreadWithState serverThreadWithState;
    @Mock
    List<Command> sender;
    @Mock
    Receiver receiver;
    @Mock
    Move move;

    @InjectMocks
    MoveToState moveToState;

    @Test
    void run_InterruptedException_interrupt() throws InterruptedException {
        when(receiver.get())
            .thenThrow(new InterruptedException());
        when(serverThreadWithState.getReceiver())
            .thenReturn(receiver);

        moveToState.run();

        verify(serverThreadWithState, times(1)).setBehaviour(any());
    }

    @Test
    void run_RuntimeException_interrupt() throws InterruptedException {
        when(receiver.get())
            .thenReturn(move);
        when(serverThreadWithState.getReceiver())
            .thenReturn(receiver);
        doThrow(new RuntimeException()).when(sender).add(any());

        moveToState.run();

        verify(serverThreadWithState, times(1)).setBehaviour(any());
    }
}
