package org.example.common.polling;

import org.example.command.Command;

public interface Receiver {
    Command get() throws InterruptedException;
}
