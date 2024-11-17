package org.example.command;

import org.example.Game;

public class ImmediatelyRetryTwice implements Command {

    private final Command command;

    public ImmediatelyRetryTwice(Command command) {
        this.command = command;
    }

    @Override
    public void execute() {
        Game.getInstance().getDeque().addFirst(command);
    }
}
