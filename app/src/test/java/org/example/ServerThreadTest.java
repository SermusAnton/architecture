package org.example;

import org.example.command.Command;
import org.example.command.Logging;
import org.example.command.Move;
import org.example.command.Retry;
import org.example.command.Rotate;
import org.example.common.polling.Receiver;
import org.example.common.polling.ServerThread;
import org.example.common.polling.SoftStop;
import org.example.handler.ExceptionHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServerThreadTest {

    @Mock
    Move move;
    @Mock
    Rotate rotate;
    @Mock
    Receiver receiver;
    @Mock
    SoftStop softStop;
    @InjectMocks
    ServerThread serverThread;

    // п.5 Проблема вертикального масштабирования и синхронизации
    @Test
    void startNewThread_success() throws InterruptedException {
        when(receiver.get())
            .thenReturn(move)
            .thenReturn(() -> serverThread.hardStop());

        var isBeforeCalled = new AtomicBoolean(false);
        serverThread.setBefore(() -> isBeforeCalled.set(true));

        var latch = new CountDownLatch(1);
        serverThread.setAfter(latch::countDown);

        serverThread.start();
        latch.await();

        assertTrue(isBeforeCalled.get());
        verify(move, times(1)).execute();
    }

    // п.6 Проблема вертикального масштабирования и синхронизации
    @Test
    void hardStopThread_success() throws InterruptedException {
        when(receiver.get())
            .thenReturn(move)
            .thenReturn(() -> serverThread.hardStop())
            .thenReturn(rotate);

        var isBeforeCalled = new AtomicBoolean(false);
        serverThread.setBefore(() -> isBeforeCalled.set(true));

        var latch = new CountDownLatch(1);
        var isAfterCalled = new AtomicBoolean(false);
        serverThread.setAfter(() ->
        {
            latch.countDown();
            isAfterCalled.set(true);
        });

        serverThread.start();
        latch.await();

        assertTrue(isBeforeCalled.get());
        verify(move, times(1)).execute();
        verify(rotate, never()).execute();
        assertTrue(isAfterCalled.get());
    }

    // п.7 Проблема вертикального масштабирования и синхронизации
    @Test
    void softStopThread_success() throws InterruptedException {
        var deque = new LinkedBlockingDeque<Command>();
        deque.add(move);
        deque.add(() -> serverThread.softStop());
        deque.add(rotate);
        serverThread = new ServerThread(deque::take, deque::isEmpty);

        var isBeforeCalled = new AtomicBoolean(false);
        serverThread.setBefore(() -> isBeforeCalled.set(true));

        var latch = new CountDownLatch(1);
        var isAfterCalled = new AtomicBoolean(false);
        serverThread.setAfter(() ->
        {
            latch.countDown();
            isAfterCalled.set(true);
        });

        serverThread.start();
        latch.await();

        assertTrue(isBeforeCalled.get());
        verify(move, times(1)).execute();
        verify(rotate, times(1)).execute();
        assertTrue(isAfterCalled.get());
    }

    @Test
    void startNewThread_InterruptedException_interrupt() throws InterruptedException {
        when(receiver.get())
            .thenThrow(new InterruptedException())
            .thenReturn(move);

        var isBeforeCalled = new AtomicBoolean(false);
        serverThread.setBefore(() -> isBeforeCalled.set(true));

        var latch = new CountDownLatch(1);
        serverThread.setAfter(latch::countDown);
        var isAfterCalled = new AtomicBoolean(false);
        serverThread.setAfter(() ->
        {
            latch.countDown();
            isAfterCalled.set(true);
        });

        serverThread.start();
        latch.await();

        assertTrue(isAfterCalled.get());
        verify(move, never()).execute();
    }

    @Test
    void startNewThread_RuntimeException_success() throws InterruptedException {
        var exception = new RuntimeException();
        when(receiver.get())
            .thenReturn(move)
            .thenThrow(new InterruptedException());
        doThrow(exception).when(move).execute();

        var isBeforeCalled = new AtomicBoolean(false);
        serverThread.setBefore(() -> isBeforeCalled.set(true));

        var latch = new CountDownLatch(1);
        serverThread.setAfter(latch::countDown);
        var isAfterCalled = new AtomicBoolean(false);
        serverThread.setAfter(() ->
        {
            latch.countDown();
            isAfterCalled.set(true);
        });

        ExceptionHandler.registerHandler("org.example.command.Move",
            "java.lang.RuntimeException",
            (c, e) -> new Retry(new Logging(c, e)));

        serverThread.start();
        latch.await();

        assertTrue(isAfterCalled.get());
        verify(move, times(1)).execute();
    }

    @Test
    void startNewThread_RuntimeException_commandNull_success() throws InterruptedException {
        when(receiver.get())
            .thenThrow(new RuntimeException())
            .thenReturn(move)
            .thenThrow(new InterruptedException());

        var isBeforeCalled = new AtomicBoolean(false);
        serverThread.setBefore(() -> isBeforeCalled.set(true));

        var latch = new CountDownLatch(1);
        serverThread.setAfter(latch::countDown);
        var isAfterCalled = new AtomicBoolean(false);
        serverThread.setAfter(() ->
        {
            latch.countDown();
            isAfterCalled.set(true);
        });

        ExceptionHandler.registerHandler("org.example.command.Move",
            "java.lang.RuntimeException",
            (c, e) -> new Retry(new Logging(c, e)));

        serverThread.start();
        latch.await();

        assertTrue(isAfterCalled.get());
        verify(move, times(1)).execute();
    }
}