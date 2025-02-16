package org.example.command.interpret;

import org.example.command.Command;

public interface Expression {
    Command interpret(Context data);
}
