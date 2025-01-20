package org.example.common.polling;

import org.example.command.Command;
import org.example.handler.ExceptionHandler;

import java.util.Objects;

public class DefaultState implements Runnable {

    private final ServerThreadWithState serverThreadWithState;

    public DefaultState(ServerThreadWithState serverThreadWithState) {
        this.serverThreadWithState = serverThreadWithState;
    }

    @Override
    public void run() {
        Command command = null;
        try {
            var receiver = serverThreadWithState.getReceiver();
            command = receiver.get();
            if (Objects.nonNull(command)) {
                command.execute();
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        } catch (RuntimeException exception) {
            assert command != null;
            ExceptionHandler.create(command, exception).execute();
        }
    }
}