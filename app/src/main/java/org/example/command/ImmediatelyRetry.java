package org.example.command;

import org.example.Game;

public class ImmediatelyRetry implements Command {

    private final Command command;

    public ImmediatelyRetry(Command command) {
        this.command = command;
    }

    @Override
    public void execute() {
        Game.getInstance().getDeque().addFirst(command);
    }
}
