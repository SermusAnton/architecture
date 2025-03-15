package org.example.common.polling;

import org.example.command.Command;
import org.example.handler.ExceptionHandler;

import java.util.Objects;

public class ServerThread {

    private final SoftStop softStop;

    private final Thread thread;

    private volatile boolean stop = false;

    private Runnable behaviour;

    private Runnable before = () -> {};

    private Runnable after = () -> {};

    public void setBefore(Runnable before) {
        this.before = before;
    }

    public void setAfter(Runnable after) {
        this.after = after;
    }

    public ServerThread(Receiver receiver, SoftStop softStop) {
        this.softStop = softStop;
        this.behaviour = () -> {
            Command command = null;
            try {
                command = receiver.get();
                assert command != null;
                command.execute();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            } catch (RuntimeException exception) {
                if (Objects.nonNull(command)) {
                    ExceptionHandler.create(command, exception).execute();
                } else {
                    ExceptionHandler.create(() -> {}, exception).execute();
                }
            }
        };
        thread = new Thread(
            () -> {
                before.run();
                while (!stop && !Thread.interrupted()) {
                    behaviour.run();
                }
                after.run();
            });
    }

    public void start() {
        thread.start();
    }

    public void hardStop() {
        stop = true;
    }

    public void softStop() {
        var oldBehavior = behaviour;
        behaviour = () -> {
            if (softStop.hasMomentCome()) {
                hardStop();
            } else {
                oldBehavior.run();
            }
        };
    }
}
