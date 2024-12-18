package org.example;

import org.example.command.Command;
import org.example.command.Retry;
import org.example.common.polling.Game;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ConcurrentLinkedDeque;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetryTest {

    @Mock
    Command command;

    @Mock
    Game game;

    @InjectMocks
    Retry retry;

    // п.5 SOLID и исключения
    @Test
    void addCommandToQueue_success() {
        try (MockedStatic<Game> staticGame = mockStatic(Game.class)) {
            staticGame.when(Game::getInstance).thenReturn(game);
            var queue = new ConcurrentLinkedDeque<Command>();
            when(game.getQueue()).thenReturn(queue);

            retry.execute();

            verify(game, times(1)).getQueue();
            assertEquals(command, queue.peek());
        }
    }

    // п.5 SOLID и исключения
    @Test
    void addCommandToEndQueue_success() {
        try (MockedStatic<Game> staticGame = mockStatic(Game.class)) {
            staticGame.when(Game::getInstance).thenReturn(game);
            var queue = new ConcurrentLinkedDeque<Command>();
            queue.add(() -> {
            });
            when(game.getQueue()).thenReturn(queue);

            retry.execute();

            verify(game, times(1)).getQueue();
            queue.poll();
            assertEquals(command, queue.peek());
        }
    }
}
