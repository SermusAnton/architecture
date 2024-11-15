package org.example;

import org.example.command.Command;
import org.example.command.ImmediatelyRetry;
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
class ImmediatelyRetryTest {

    @Mock
    Command command;

    @Mock
    Game game;

    @InjectMocks
    ImmediatelyRetry immediatelyRetry;

    // п.6 SOLID и исключения
    @Test
    void addCommandToHeadQueue_success() {
        try (MockedStatic<Game> staticGame = mockStatic(Game.class)) {
            staticGame.when(Game::getInstance).thenReturn(game);
            var queue = new ConcurrentLinkedDeque<Command>();
            queue.add(() -> {
            });
            when(game.getDeque()).thenReturn(queue);

            immediatelyRetry.execute();

            verify(game, times(1)).getDeque();
            assertEquals(command, queue.peek());
        }
    }
}
