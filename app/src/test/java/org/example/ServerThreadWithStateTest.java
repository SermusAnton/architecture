package org.example;

import org.example.command.Command;
import org.example.command.Move;
import org.example.command.MoveToCommand;
import org.example.command.Rotate;
import org.example.command.RunCommand;
import org.example.common.polling.Receiver;
import org.example.common.polling.ServerThreadWithState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServerThreadWithStateTest {

    @Mock
    Move move;
    @Mock
    Rotate rotate;
    @Mock
    Receiver receiver;
    @InjectMocks
    ServerThreadWithState serverThreadWithState;

    // п.5 Состояние
    @Test
    void startNewThreadWithState_hardStop() throws InterruptedException {
        when(receiver.get())
            .thenReturn(move)
            .thenReturn(() -> serverThreadWithState.hardStop())
            .thenReturn(rotate);

        var isBeforeCalled = new AtomicBoolean(false);
        serverThreadWithState.setBefore(() -> isBeforeCalled.set(true));

        var latch = new CountDownLatch(1);
        serverThreadWithState.setAfter(latch::countDown);

        serverThreadWithState.start();
        latch.await();

        assertTrue(isBeforeCalled.get());
        verify(move, times(1)).execute();
        verify(rotate, never()).execute();
    }

    // п.6 Состояние
    @Test
    void startNewThreadWithState_MoveToCommand() throws InterruptedException {
        var sender = new ArrayList<Command>();
        var moveToCommand = new MoveToCommand(serverThreadWithState, sender);

        when(receiver.get())
            .thenReturn(moveToCommand)
            .thenReturn(move)
            .thenReturn(rotate)
            .thenReturn(null)
            .thenReturn(() -> serverThreadWithState.hardStop());

        var isBeforeCalled = new AtomicBoolean(false);
        serverThreadWithState.setBefore(() -> isBeforeCalled.set(true));

        var latch = new CountDownLatch(1);
        serverThreadWithState.setAfter(latch::countDown);

        serverThreadWithState.start();
        latch.await();

        assertTrue(isBeforeCalled.get());
        verify(move, never()).execute();
        verify(rotate, never()).execute();
        assertTrue(sender.size() == 2);
        assertTrue(sender.stream().anyMatch(command -> command.equals(move)));
        assertTrue(sender.stream().anyMatch(command -> command.equals(rotate)));
    }

    // п.7 Состояние
    @Test
    void startNewThreadWithState_RunCommand() throws InterruptedException {
        var sender = new ArrayList<Command>();
        var moveToCommand = new MoveToCommand(serverThreadWithState, sender);
        var runCommand = new RunCommand(serverThreadWithState);

        when(receiver.get())
            .thenReturn(moveToCommand)
            .thenReturn(move)
            .thenReturn(rotate)
            .thenReturn(null)
            .thenReturn(runCommand)
            .thenReturn(move)
            .thenReturn(() -> serverThreadWithState.hardStop());

        var isBeforeCalled = new AtomicBoolean(false);
        serverThreadWithState.setBefore(() -> isBeforeCalled.set(true));

        var latch = new CountDownLatch(1);
        serverThreadWithState.setAfter(latch::countDown);

        serverThreadWithState.start();
        latch.await();

        assertTrue(isBeforeCalled.get());
        verify(move, times(1)).execute();
        verify(rotate, never()).execute();
        assertTrue(sender.size() == 2);
        assertTrue(sender.stream().anyMatch(command -> command.equals(move)));
        assertTrue(sender.stream().anyMatch(command -> command.equals(rotate)));
    }
}