package org.example.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logging implements Command {

    private final Command command;

    private final Exception exception;

    private static final Logger logger = LoggerFactory.getLogger(Logging.class);

    public Logging(Command command, Exception exception) {
        this.command = command;
        this.exception = exception;
    }

    @Override
    public void execute() {
        logger.error("Command {} throw {}", command.getClass().getName(), exception.getMessage());
    }
}
