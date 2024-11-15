package org.example.command;

import org.example.Game;

public class Retry implements Command {

    private final Command command;

    public Retry(Command command) {
        this.command = command;
    }

    @Override
    public void execute() {
        Game.getInstance().getQueue().add(command);
    }
}
