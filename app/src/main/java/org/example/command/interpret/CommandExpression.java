package org.example.command.interpret;

import org.example.command.Command;
import org.example.common.ioc.IoC;

public class CommandExpression implements Expression {

    private final String commandName;

    public CommandExpression(String commandName){
        this.commandName = commandName;
    }

    @Override
    public Command interpret(Context data) {
        var id = Long.parseLong(data.getDataBy("id").toString());
        var gameObject = IoC.resolve("GameObject", id);
        var args = data.getDataBy("args");
        return IoC.resolve(commandName, gameObject, args);
    }
}
