package org.example.command.interpret;

import org.example.command.Command;
import org.example.command.MacroCommand;

import java.util.List;

public class MacroCommandExpression  implements Expression {

    private final  List<Expression> expressions;

    public MacroCommandExpression(List<Expression> expressions) {
        this.expressions = expressions;
    }
    @Override
    public Command interpret(Context data) {
        var commands = expressions.stream().map(expression -> expression.interpret(data)).toList();
        return new MacroCommand(commands);
    }
}
