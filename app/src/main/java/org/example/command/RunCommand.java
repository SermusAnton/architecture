package org.example.command;

import org.example.common.polling.DefaultState;
import org.example.common.polling.ServerThreadWithState;

public class RunCommand implements Command {

    private final ServerThreadWithState serverThreadWithState;

    public RunCommand(ServerThreadWithState serverThreadWithState) {
        this.serverThreadWithState = serverThreadWithState;
    }

    @Override
    public void execute() {
        serverThreadWithState.setBehaviour(new DefaultState(serverThreadWithState));
    }
}