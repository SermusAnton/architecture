package org.example.command;

import org.example.common.ioc.IoC;
import org.example.common.polling.ServerThread;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class NewServerThread implements Command {

    private final BlockingDeque<Command> deque;

    private final ServerThread serverThread;

    private final Long id;

    public NewServerThread(Long id) {
        this.id = id;
        this.deque = new LinkedBlockingDeque<>();
        this.serverThread = new ServerThread(deque::take, deque::isEmpty);
    }

    @Override
    public void execute() {
        IoC.<Command>resolve(
            "IoC.Register",
            String.format("Game%s.Deque",  id),
                        deque
        );
        IoC.<Command>resolve(
            "IoC.Register",
            String.format("Game%s.ServerThread",  id),
            serverThread
        );
        serverThread.start();
    }
}
