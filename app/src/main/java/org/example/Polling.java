package org.example;

import org.example.handler.ExceptionHandler;

public class Polling {

    public volatile boolean stop = false;

    public void polling() {
        var queue = Game.getInstance().getQueue();
        while (!stop) {
            var command = queue.poll();
            try {
                assert command != null;
                command.execute();
            } catch (RuntimeException exception) {
                ExceptionHandler.create(command, exception).execute();
            }
        }
    }
}
