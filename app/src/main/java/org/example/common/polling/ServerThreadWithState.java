package org.example.common.polling;

import java.util.Objects;

public class ServerThreadWithState {

    private final Thread thread;

    private Runnable behaviour;

    private final Receiver receiver;

    private Runnable before = () -> {
    };

    private Runnable after = () -> {
    };

    public void setBefore(Runnable before) {
        this.before = before;
    }

    public void setAfter(Runnable after) {
        this.after = after;
    }

    public ServerThreadWithState(Receiver receiver) {
        this.receiver = receiver;
        this.behaviour = new DefaultState(this);
        thread = new Thread(
            () -> {
                before.run();

                while (Objects.nonNull(behaviour)) {
                    behaviour.run();
                }
                after.run();
            });
    }

    public void start() {
        thread.start();
    }

    public void setBehaviour(Runnable behaviour) {
        this.behaviour = behaviour;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void hardStop() {
        setBehaviour(null);
    }
}