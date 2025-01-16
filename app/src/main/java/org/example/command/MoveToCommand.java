package org.example.command;

import org.example.common.polling.MoveToState;
import org.example.common.polling.ServerThreadWithState;

import java.util.List;

public class MoveToCommand implements Command {

    private final ServerThreadWithState serverThreadWithState;

    private final List<Command> sender;

    public MoveToCommand(ServerThreadWithState serverThreadWithState, List<Command> sender) {
        this.serverThreadWithState = serverThreadWithState;
        this.sender = sender;
    }

    @Override
    public void execute() {
        serverThreadWithState.setBehaviour(new MoveToState(serverThreadWithState, sender));
    }
}