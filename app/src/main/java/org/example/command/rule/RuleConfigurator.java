package org.example.command.rule;

import org.example.command.Command;
import org.example.command.MacroCommand;
import org.example.common.ioc.IoC;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class RuleConfigurator {

    private final Map<String, List<String>> objectAbility;
    private final Map<String, List<String>> macroCommands;

    public RuleConfigurator(Map<String, List<String>> objectAbility, Map<String, List<String>> macroCommands) {
        this.objectAbility = objectAbility;
        this.macroCommands = macroCommands;
    }

    public void execute() {
        macroCommands.forEach(this::mapMacroCommand);
        objectAbility.forEach(this::mapObjectAbility);
    }

    private void mapMacroCommand(String name, List<String> commandNames) {
        var functions = commandNames.stream()
            .map(n -> IoC.<Function<List<Object>, Command>> resolve("GetCommandByName", n))
            .toList();
        Function<List<Object>, Command> createMacro = args -> {
            var commands = functions.stream().map(f -> f.apply(args)).toList();
            return new MacroCommand(commands);
        };
        IoC.resolve( "SetCommandByName", name, createMacro);
    }

    private void mapObjectAbility(String name, List<String> commandNames) {
        var functions = commandNames.stream()
            .map(n -> IoC.<Function<List<Object>, Command>>resolve( "GetCommandByName", n))
            .toList();
        IoC.resolve("SetAvailableCommandByType", name, functions);
    }
}
