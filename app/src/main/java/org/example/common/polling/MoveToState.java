package org.example.common.polling;

import org.example.command.Command;
import org.example.handler.ExceptionHandler;

import java.util.Objects;
import java.util.List;

public class MoveToState implements Runnable {

    private final ServerThreadWithState serverThreadWithState;

    private final List<Command> sender;

    public MoveToState(ServerThreadWithState serverThreadWithState, List<Command> sender) {
        this.serverThreadWithState = serverThreadWithState;
        this.sender = sender;
    }

    @Override
    public void run() {
        Command command = null;
        try {
            var receiver = serverThreadWithState.getReceiver();
            command = receiver.get();
            while (Objects.nonNull(command)) {
                sender.add(command);
                command = receiver.get();
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        } catch (RuntimeException exception) {
            assert command != null;
            ExceptionHandler.create(command, exception).execute();
        }
        serverThreadWithState.setBehaviour(new DefaultState(serverThreadWithState));
    }
}