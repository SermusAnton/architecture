package org.example.command;

import org.example.common.ioc.IoC;
import org.example.common.polling.ServerThread;

public class SoftStopServerThread implements Command {
    private final Long id;

    public SoftStopServerThread(Long id) {
        this.id = id;
    }

    @Override
    public void execute() {
        var serverThread = IoC.<ServerThread>resolve(
            String.format("Game%s.ServerThread", id)
        );
        serverThread.softStop();
    }
}
