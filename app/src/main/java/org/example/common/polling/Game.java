package org.example.common.polling;

import org.example.command.Command;

import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Game {

    private final ConcurrentLinkedDeque<Command> queue = new ConcurrentLinkedDeque<>();

    public static class GameHolder {
        public static final Game HOLDER_INSTANCE = new Game();
    }

    public static Game getInstance() {
        return GameHolder.HOLDER_INSTANCE;
    }

    public Queue<Command> getQueue() {
        return queue;
    }

    public Deque<Command> getDeque() {
        return queue;
    }
}
